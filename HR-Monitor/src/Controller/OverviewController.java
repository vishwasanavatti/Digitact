package Controller;

import Model.MVC.OverviewModel;
import Model.User.ApplicantUI;
import Util.Dictionary.ApplicantInfoDictionary;
import Util.Dictionary.BasicInfoDictionary;
import Util.Dictionary.IDictionary;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OverviewController {
    Stage stage;
    OverviewModel model;
    // Create a TableView with a list of Applicants
    @FXML
    TableView<ApplicantUI> userTable;
    private ObservableList<ApplicantUI> observableListTableView;
    IDictionary biDic, aiDic;
    // Overview of all Applicants
    @FXML
    TableColumn<ApplicantUI, Number> idCol = new TableColumn<>("id");
    @FXML
    TableColumn<ApplicantUI, String> firstNameCol = new TableColumn<>("First Name");
    @FXML
    TableColumn<ApplicantUI, String> lastNameCol = new TableColumn<>("Last Name");
    @FXML
    TableColumn<ApplicantUI, String> status = new TableColumn<>("Status");

    Pane root;

    public OverviewController(/* Stage parentStage */) throws IOException {
        model = new OverviewModel();
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/storageView.fxml"));
        loader.setController(this);
        root = (Pane) loader.load();
        setDictionary();
        getTable();
    }

    private void setDictionary() {
        biDic = new BasicInfoDictionary();
        aiDic = new ApplicantInfoDictionary();
    }

    public void setFactoriesAndComparatorsForTableColumns() {
        idCol.setCellValueFactory(user -> new ReadOnlyLongWrapper(user.getValue().getID()));
        idCol.setVisible(false);

        firstNameCol.setCellValueFactory(
                user -> new ReadOnlyStringWrapper(user.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(
                user -> new ReadOnlyStringWrapper(user.getValue().getLastName()));
        status.setCellValueFactory(
                user -> new ReadOnlyStringWrapper(IDictionary.getTranslation(aiDic, user.getValue().getStatus().toString())));
        firstNameCol.setText(IDictionary.getTranslation(biDic, "First Name"));
        lastNameCol.setText(IDictionary.getTranslation(biDic, "Last Name"));
    }

    public ObservableList<ApplicantUI> getTable() {
        List<ApplicantUI> applicantsList = model.getDB();
        AddClickFunctionToUserTable();
        observableListTableView = userTable.getItems();
        observableListTableView.clear();
        observableListTableView.addAll(applicantsList);
        setFactoriesAndComparatorsForTableColumns();
        return observableListTableView;
    }

    public Pane getPane() {
        return root;
    }

    private void AddClickFunctionToUserTable() {
        userTable.setRowFactory(
                e -> {
                    TableRow<ApplicantUI> row = new TableRow<>();
                    row.setOnMouseClicked(
                            event -> {
                                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                    ApplicantUI rowData = row.getItem();
                                    new ApplicantInfoController(rowData.getID(), model);
                                }
                            });
                    return row;
                });
    }
}
