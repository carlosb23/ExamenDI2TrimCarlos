package com.example.examendi.controllers;

import com.example.examendi.domain.cliente.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PantallaPrincipal implements Initializable {

    @FXML
    private TextField txtnombre;
    @FXML
    private ChoiceBox<String> chiceboxsexo;
    @FXML
    private ChoiceBox<String> choiseboxactividad;
    @FXML
    private TextField txtpeso;
    @FXML
    private TextField txtaltura;
    @FXML
    private Spinner<Integer> spinneredad;
    @FXML
    private TextField txtobsevaciones;
    @FXML
    private Button buttonguardar;
    @FXML
    private Label infolabel;
    @FXML
    private Button buttoninforme;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chiceboxsexo.getItems().addAll("Hombre", "Mujer");

        // Configurar ChoiceBox para el tipo de actividad
        choiseboxactividad.getItems().addAll("Sedentario", "Moderado", "Activo", "Muy Activo");

        SpinnerValueFactory<Integer> edadFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 120);
        spinneredad.setValueFactory(edadFactory);
    }

    @FXML
    public void guardar(ActionEvent actionEvent) {

        if (txtnombre.getText().isEmpty() || chiceboxsexo.getValue() == null ||
                choiseboxactividad.getValue() == null || txtpeso.getText().isEmpty() ||
                txtaltura.getText().isEmpty() || spinneredad.getValue() == null ||
                txtobsevaciones.getText().isEmpty()) {
            infolabel.setText("Por favor, complete todos los campos.");
            return;
        }

        HashMap<String, HashMap<String, Double>> actividadMap = new HashMap<>();

        String nombre = txtnombre.getText();
        String sexo = chiceboxsexo.getValue();
        String tipoActividad = choiseboxactividad.getValue();
        double peso = Double.parseDouble(txtpeso.getText());
        double altura = Double.parseDouble(txtaltura.getText());
        int edad = spinneredad.getValue();
        String observaciones = txtobsevaciones.getText();

        double GER;
        if (sexo.equals("Hombre")) {
            GER = 66.473 + 13.751 * peso + 5.0033 * altura - 6.755 * edad;
        } else {
            GER = 655.0955 + 9.463 * peso + 1.8496 * altura - 4.6756 * edad;
        }

        //TODO hacer hashmap
        HashMap<String, Double> hombreActividadMap = new HashMap<>();
        hombreActividadMap.put("Sedentario", 1.3);
        hombreActividadMap.put("Moderado", 1.6);
        hombreActividadMap.put("Activo", 1.7);
        hombreActividadMap.put("Muy Activo", 2.1);
        actividadMap.put("Hombre", hombreActividadMap);

        // Mujer
        HashMap<String, Double> mujerActividadMap = new HashMap<>();
        mujerActividadMap.put("Sedentario", 1.3);
        mujerActividadMap.put("Moderado", 1.5);
        mujerActividadMap.put("Activo", 1.6);
        mujerActividadMap.put("Muy Activo", 1.9);
        actividadMap.put("Mujer", mujerActividadMap);

        double GET = actividadMap.get(sexo).get(tipoActividad) * GER;


        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setSexo(sexo);
        cliente.setTipoactividad(tipoActividad);
        cliente.setPeso(peso);
        cliente.setTalla(altura);
        cliente.setEdad(edad);
        cliente.setObservaciones(observaciones);
        cliente.setGER(GER);
        cliente.setGET(GET);

        // Mostrar un mensaje de éxito
        infolabel.setText(String.format("El cliente con nombre %s tiene un GER de %.2f y un GET de %.2f", nombre, GER, GET));
    }

    @FXML
    public void descargarinforme(ActionEvent actionEvent) throws SQLException, JRException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdexamendi", "root", "");
        JasperPrint jasperPrint = JasperFillManager.fillReport("informeClientes.jasper", null, connection);

        // Mostrar el informe en una ventana
        JasperViewer.viewReport(jasperPrint, false);

        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("datos_clientes_clinica.pdf"));
        exp.setConfiguration(new SimplePdfExporterConfiguration());
        exp.exportReport();
    }
}