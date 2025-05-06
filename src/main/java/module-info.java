module com.edu.pucpr.morsecode {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.edu.pucpr.morsecode to javafx.fxml;
    exports com.edu.pucpr.morsecode;
}