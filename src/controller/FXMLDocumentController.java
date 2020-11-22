/*
 * The following blocks of code were copied from quiz three
 */
package controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.persistence.Query;
import model.Student;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    
    @FXML
    private Button buttonCreateStudent;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonRead;
    
    @FXML
    private Button buttonSearch;

    @FXML
    private Button buttonReadByID;

    @FXML
    private Button buttonReadByName;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonReadByNameCGPA;
    
 // The following block of code was copied and modified from the quiz 4 source code, it is not fully my own work
     @FXML
    private TextField textboxNames;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> name;
    @FXML
    private TableColumn<Student, Integer> id;
    @FXML
    private TableColumn<Student, Float> cgpa;

    private ObservableList<Student> studentData;
    public void setTableData(List<Student> studentList) {

        studentData = FXCollections.observableArrayList();
        studentList.forEach(s -> {
            studentData.add(s);
        });
        studentTable.setItems(studentData);
        studentTable.refresh(); }

    
    
    @FXML
    void searchWithName(ActionEvent event) {
        System.out.println("You clicked seach! ");    
        String name = textboxNames.getText();
        List<Student> students = readByName(name);
        if (students == null || students.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Box");
            alert.setHeaderText("Writing heading");
            alert.setContentText("There is no No student");
            alert.showAndWait();
        } else {

            // setting the table
            setTableData(students);
        }
    }
    
    


    @FXML
    void createStudent(ActionEvent event) {
        Scanner input = new Scanner(System.in);
        

        System.out.println("enter ID number :");
        int id = input.nextInt();
        
        System.out.println("enter name:");
        String name = input.next();
        
        System.out.println("enter GPA:");
        double cgpa = input.nextDouble();

        Student student = new Student();
        
        student.setId(id);
        student.setName(name);
        student.setCgpa(cgpa);
        create(student);

    }

    @FXML
    void deleteStudent(ActionEvent event) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter ID:");
        int id = input.nextInt();
        
        Student s = readById(id);
        System.out.println("we are deleting this student: "+ s.toString());
        delete(s);

    }
    

    @FXML
    void readByID(ActionEvent event) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter ID:");
        int id = input.nextInt();
        
        Student s = readById(id);
        System.out.println(s.toString());

    }

    @FXML
    void readByName(ActionEvent event) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Name:");
        String name = input.next();
        
        List<Student> s = readByName(name);
        System.out.println(s.toString());

    }

    @FXML
    void readByNameCGPA(ActionEvent event) {
        
        Scanner input = new Scanner(System.in);
       
        
        System.out.println("Enter Name:");
        String name = input.next();
        
        System.out.println("Enter CPGA:");
        double cgpa = input.nextDouble();
        List<Student> students =  readByNameAndCGPA(name, cgpa);

    }

    @FXML
    void readStudent(ActionEvent event) {

    }

    @FXML
    void updateStudent(ActionEvent event) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter ID:");
        int id = input.nextInt();
        
        System.out.println("Enter Name:");
        String name = input.next();
        
        System.out.println("Enter CPGA:");
        double cgpa = input.nextDouble();

        Student student = new Student();

        student.setId(id);
        student.setName(name);
        student.setCgpa(cgpa);
  
        update(student);

    }
    

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");

        Query query = manager.createNamedQuery("Student.findAll");
        List<Student> data = query.getResultList();

        for (Student s : data) {
            System.out.println(s.getId() + " " + s.getName() + " " + s.getCgpa());
        }
    }
    
    
    
    
        @FXML
    private void handleButtonAction2(ActionEvent event) {
        System.out.println("You clicked me!");

    }
    
    // Database manager
    EntityManager manager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loading students from database
        //database reference: "LorenzoPaulFXPU"
        manager = (EntityManager) Persistence.createEntityManagerFactory("LorenzoPaulFXPU").createEntityManager();

    }

    /*
    Implementing CRUD operations
    */

    public void create(Student student) {
        try {
            manager.getTransaction().begin();
            
            if (student.getId() != null) {
                

                manager.persist(student);
                manager.getTransaction().commit();
                
                System.out.println(student.toString() + " is created");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Student> readAll(){
        Query query = manager.createNamedQuery("Student.findAll");
        List<Student> students = query.getResultList();

        for (Student s : students) {
            System.out.println(s.getId() + " " + s.getName() + " " + s.getCgpa());
        }
        
        return students;
    }
    
    public Student readById(int id){
        Query query = manager.createNamedQuery("Student.findById");
        
        // setting query parameter
        query.setParameter("id", id);
        
        // execute query
        Student student = (Student) query.getSingleResult();
        if (student != null) {
            System.out.println(student.getId() + " " + student.getName() + " " + student.getCgpa());
        }
        
        return student;
    }        
    
    public List<Student> readByName(String name){
        Query query = manager.createNamedQuery("Student.findByName");
        
        // setting query parameter
        query.setParameter("name", name);
        
        // execute query
        List<Student> students =  query.getResultList();
        for (Student student: students) {
            System.out.println(student.getId() + " " + student.getName() + " " + student.getCgpa());
        }
        
        return students;
    }        
    
    public List<Student> readByNameAndCGPA(String name, double cpga){
        Query query = manager.createNamedQuery("Student.findByNameAndCgpa");
        

        query.setParameter("cgpa", cpga);
        query.setParameter("name", name);
        

        List<Student> students =  query.getResultList();
        for (Student student: students) {
            System.out.println(student.getId() + " " + student.getName() + " " + student.getCgpa());
        }
        
        return students;
    }        
    
    
    public void update(Student model) {
        try {

            Student existingStudent = manager.find(Student.class, model.getId());

            if (existingStudent != null) {
                // begin transaction
                manager.getTransaction().begin();
                existingStudent.setName(model.getName());
                existingStudent.setCgpa(model.getCgpa());
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void delete(Student student) {
        try {
            Student existingStudent = manager.find(Student.class, student.getId());

            if (existingStudent != null) {
                
                manager.getTransaction().begin();
                manager.remove(existingStudent);
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
