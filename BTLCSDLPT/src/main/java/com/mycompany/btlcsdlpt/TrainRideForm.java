package com.mycompany.btlcsdlpt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TrainRideForm extends javax.swing.JFrame implements ActionListener{
    private static DefaultTableModel tblModel;
    private static DefaultTableModel tblModel2;
    private String status = "Trạng thái";
    
    ArrayList<TrainRide> list = new ArrayList<>();
    ArrayList<Employee> listEmployee = new ArrayList<>();
    
    public TrainRideForm() {
        initComponents();
        btnCancel.addActionListener((ActionListener) this);
        btnSelect.addActionListener((ActionListener) this);
        btnUpdateEmployee.addActionListener(this);
        tblModel = (DefaultTableModel) tblTrainRide.getModel();
        tblModel2 = (DefaultTableModel) tblEmployee.getModel();
        
        //uneditable table
        tblTrainRide.setCellSelectionEnabled(false);
        tblTrainRide.setRowSelectionAllowed(true);
        tblTrainRide.setEnabled(false);
        

        tblEmployee.setRowSelectionAllowed(true); // Cho phép chọn hàng
        tblEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtIdE.setEnabled(false);
        
        //table TrainRide
        fillDataToTable();
        getAllDbEmployee();
        
        //table Employee
        fillDataToTable2();
        loadCombobox();
        
        lbStatus.setText(status);
    }
    
    public Query getAllDb(String Train_Station_Class, String date, int Class_1_seats) {
        Connection db = MyConnection.dbConn();
        
        try {
            String sql = "SELECT StationTrainRideTG.id, StationTrainRideTG.DepatureStation, StationTrainRideTG.DepatureTime FROM \n" +
"		(SELECT TrainRideTG.id, TrainRideTG.DepatureStation, TrainRideTG.DepatureTime, TrainRideTG.IdTrain\n" +
"			FROM (\n" +
"				SELECT Station.Name FROM [QLTH].[QLTH].dbo.Station AS Station WHERE Station.Train_station_class = " + "'" + Train_Station_Class + "'" + ") StationTG\n" +
"				INNER JOIN (\n" +
"					SELECT TrainRide21.id, TrainRide21.DepatureStation, TrainRide21.DepatureTime, TrainRide22.IdTrain \n" +
"						FROM [QLTH_TrainRide21].dbo.TrainRide TrainRide21\n" +
"						INNER JOIN [LINK_QLTH_HY].[QLTH_TrainRide22].dbo.TrainRide TrainRide22 ON TrainRide21.id = TrainRide22.id\n" +
"					WHERE CAST(TrainRide21.DepatureTime AS DATE) = " + "'" + date + "'" + ") TrainRideTG\n" +
"			ON StationTG.Name = TrainRideTG.DepatureStation) StationTrainRideTG\n" +
"		INNER JOIN\n" +
"		(SELECT Train.ID \n" +
"		FROM [QLTH].[QLTH].dbo.Train AS Train WHERE Train.Class_1_seats > " + "'" + Class_1_seats + "'" + ") TrainTG\n" +
"			\n" +
"	ON StationTrainRideTG.IdTrain = TrainTG.ID";

            // Tạo đối tượng Statement để thực hiện câu truy vấn
            Statement stmt = db.createStatement();

            // Thực hiện câu truy vấn và lấy dữ liệu
            ResultSet rs = stmt.executeQuery(sql);

            // Hiển thị dữ liệu lấy được
            while (rs.next()) {
                String column1 = rs.getString("id");
                String column2 = rs.getString("DepatureStation");
                Time column3 = rs.getTime("DepatureTime");
                list.add(new TrainRide(column1, column2, column3));
            }
            list.forEach(System.out::println);
            if (rs != null) {
                    rs.close();
                }
                // Đóng Statement
                if (stmt != null) {
                    stmt.close();
                }
                // Đóng kết nối đến cơ sở dữ liệu
                if (db != null) {
                    db.close();
                }
            return new Query(list, "Query thành công", true);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new Query(new ArrayList<>(), "Query bị lỗi", false);
        } catch (Exception e) {
            e.printStackTrace();
            return new Query(new ArrayList<>(), "Không thể kết nối với database", false);
        }
    }
    
    public void getAllDbEmployee() {
        Connection db = MyConnection.dbConn();
        
        try {
            String sql = "SELECT * FROM [QLTH_TB].dbo.Employee";

            // Tạo đối tượng Statement để thực hiện câu truy vấn
            Statement stmt = db.createStatement();

            // Thực hiện câu truy vấn và lấy dữ liệu
            ResultSet rs = stmt.executeQuery(sql);

            // Hiển thị dữ liệu lấy được
            while (rs.next()) {
                String column1 = rs.getString("id_e");
                String column2 = rs.getString("FullName");
                String column3 = rs.getString("Role");
                String column4 = rs.getString("phoneNumber");
                String column5 = rs.getString("id_branch");
                listEmployee.add(new Employee(column1, column2, column3, column4, column5));
            }
            listEmployee.forEach(System.out::println);
            fillDataToTable2();
            if (rs != null) {
                    rs.close();
                }
                // Đóng Statement
                if (stmt != null) {
                    stmt.close();
                }
                // Đóng kết nối đến cơ sở dữ liệu
                if (db != null) {
                    db.close();
                }   
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void fillDataToTable() {
        String[] columns = {"No.", "id", "Depature Station", "Depature Time"};
        tblModel.setColumnIdentifiers(columns);
        tblModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            tblModel.addRow(new Object[]{
                i + 1,
                list.get(i).getId(),
                list.get(i).getDepatureStation(),
                list.get(i).getDepatureTime(),
            });
        }
    }
    
    public void fillDataToTable2() {
        String[] columns = {"STT", "Mã nhân viên", "Họ và tên", "Chức vụ", "Số điện thoại", "Mã chi nhánh"};
        tblModel2.setColumnIdentifiers(columns);
        tblModel2.setRowCount(0);
        for (int i = 0; i < listEmployee.size(); i++) {
            tblModel2.addRow(new Object[]{
                i + 1,
                listEmployee.get(i).getId(),
                listEmployee.get(i).getFullname(),
                listEmployee.get(i).getRole(),
                listEmployee.get(i).getPhonenumber(),
                listEmployee.get(i).getId_branch(),
            });
        }
        tblEmployee.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tblEmployee.getSelectedRow() >= 0) {
                    txtIdE.setText(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1) + "");
                    txtFullnameE.setText(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 2) + "");
                    txtRole.setText(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 3) + "");
                    cbPhonenumberE.setText(tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 4) + "");
                    cbIdbranchE.setSelectedItem(tblEmployee.getModel().getValueAt(tblEmployee.getSelectedRow(), 5) + "");
                }
            }
        }); 
    }
  
    private void btnSelectOnClick() {
        list.clear();
        int departureTimeIdx = cbDepatureTime.getSelectedIndex();
        int train_station_classIdx = cbTrainStationClass.getSelectedIndex();
        int class_1_seatsIdx = cbClass1Seats.getSelectedIndex();
        
        String departureTime = "";
        String train_station_class = "";
        int class_1_seats = 0;
        
        switch (departureTimeIdx) {
            case 0:
                departureTime = "2023-01-05";
                break;
            case 1:
                departureTime = "2023-10-01";
                break;
            case 2:
                departureTime = "2023-12-06";
                break;
            case 3:
                departureTime = "2023-11-28";
                break;
            default:
                break;
        }
        
        if (train_station_classIdx == 0) train_station_class = "A";
        else if (train_station_classIdx == 1) train_station_class = "B";
        
        switch (class_1_seatsIdx) {
            case 0:
                class_1_seats = 0;
                break;
            case 1:
                class_1_seats = 30;
                break;
            case 2:
                class_1_seats = 60;
                break;
            case 3:
                class_1_seats = 90;
                break;
            default:
                break;
        }
        
        Query query = new Query(new ArrayList<>(), "Chưa kết nối", false);
        status = query.getMessage();
        lbStatus.setText(status);
        query = getAllDb(train_station_class, departureTime, class_1_seats);
        System.out.println(query.getMessage());
        System.out.println("---" + departureTime + "---" + train_station_class + "---"
        + class_1_seats + "---");
        list = query.getList();
        status = query.getMessage();
        lbStatus.setText(status);
        fillDataToTable();
    }

    private void btnCancelOnClick() {
        new TrainRideForm().setVisible(true);
        this.dispose();
    }
    
    private void btnUpdateEmployee() {
        try {
            Connection conn = MyConnection.dbConn();
            PreparedStatement ps = conn.prepareStatement("  UPDATE [QLTH_TB].dbo.Employee SET FullName = ?, Role = ?, phoneNumber = ?, id_branch = ? WHERE id_e = ?");
            ps.setString(1, txtFullnameE.getText());
            ps.setString(2, txtRole.getText());
            ps.setString(3, cbPhonenumberE.getText());
            ps.setString(4, cbIdbranchE.getSelectedItem().toString());
            ps.setString(5, txtIdE.getText());
           
            ps.executeUpdate();
            System.out.println("Update query");
            listEmployee.clear();
            getAllDbEmployee();
            JOptionPane.showMessageDialog(this, "Cập nhật Employee thành công");
            fillDataToTable2();
        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = ex.getMessage(); // Nội dung lỗi
            JOptionPane.showMessageDialog(this, errorMessage);
            System.out.println("Error message: " + errorMessage);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnSelect)){
            btnSelectOnClick();
        }
        
        if(e.getSource().equals(btnCancel)){
            btnCancelOnClick();
        }
                
        
        if(e.getSource().equals(btnUpdateEmployee)){
            btnUpdateEmployee();
        }
        
    }

    public void loadCombobox() {
        try {
            Connection conn = MyConnection.dbConn();
            
            PreparedStatement ps1 = conn.prepareStatement("SELECT Branch.id_branch FROM [QLTH_REP_TB].dbo.Branch AS Branch");
            ResultSet rs1 = ps1.executeQuery();

            while(rs1.next()) {
                cbIdbranchE.addItem(rs1.getString("id_branch"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbDepatureTime = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbTrainStationClass = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbClass1Seats = new javax.swing.JComboBox<>();
        btnSelect = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lbStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTrainRide = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtIdE = new javax.swing.JTextField();
        txtFullnameE = new javax.swing.JTextField();
        cbPhonenumberE = new javax.swing.JTextField();
        cbIdbranchE = new javax.swing.JComboBox<>();
        btnUpdateEmployee = new javax.swing.JButton();
        txtRole = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Liệt kê thông tin mã chuyến tàu, ga xuất phát, thời gian xuất phát của chuyến tàu có:");

        jLabel3.setText("Thời gian khởi hành:");

        cbDepatureTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2023-01-05", "2023-10-01", "2023-12-06", "2023-11-28" }));
        cbDepatureTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDepatureTimeActionPerformed(evt);
            }
        });

        jLabel4.setText("Hạng ga tàu:");

        cbTrainStationClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "B" }));

        jLabel5.setText("Số chỗ loại 1 tối thiều: ");

        cbClass1Seats.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "30", "60", "90" }));

        btnSelect.setText("Tìm kiếm");
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy bỏ");

        lbStatus.setText("Kết nối");

        tblTrainRide.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ){
            public boolean isCelleditable(int row, int column) {
                return false;
            }
        }
    );
    jScrollPane1.setViewportView(tblTrainRide);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel3)
                    .addGap(32, 32, 32)
                    .addComponent(cbDepatureTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addGap(86, 86, 86)
                    .addComponent(cbTrainStationClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(74, 74, 74)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addGap(18, 18, 18)
                    .addComponent(cbClass1Seats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(btnSelect)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel)))
            .addGap(0, 0, Short.MAX_VALUE))
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(271, 271, 271)
                    .addComponent(lbStatus)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(100, 100, 100))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(cbDepatureTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5)
                .addComponent(cbClass1Seats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTrainStationClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelect)
                    .addComponent(btnCancel)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lbStatus)
            .addGap(18, 18, 18)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(17, Short.MAX_VALUE))
    );

    tabPane.addTab("TrainRide", jPanel1);

    jLabel8.setText("Danh sách nhân viên tại Branch Thái Bình");

    tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    jScrollPane2.setViewportView(tblEmployee);

    jLabel9.setText("Mã nhân viên:");

    jLabel10.setText("Họ và tên:");

    jLabel11.setText("Chức vụ:");

    jLabel12.setText("Số điện thoại:");

    jLabel13.setText("Mã chi nhánh:");

    cbPhonenumberE.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbPhonenumberEActionPerformed(evt);
        }
    });

    btnUpdateEmployee.setText("Cập nhật");

    txtRole.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtRoleActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(67, 67, 67))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(46, 46, 46)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(74, 74, 74)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtIdE, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addComponent(txtFullnameE)
                        .addComponent(txtRole))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(18, 18, 18)
                            .addComponent(cbPhonenumberE, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnUpdateEmployee)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(cbIdbranchE, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(174, 174, 174)
                    .addComponent(jLabel8))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(22, 22, 22)
            .addComponent(jLabel8)
            .addGap(32, 32, 32)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(jLabel12)
                .addComponent(txtIdE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbPhonenumberE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel10)
                .addComponent(jLabel13)
                .addComponent(txtFullnameE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbIdbranchE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11)
                .addComponent(btnUpdateEmployee)
                .addComponent(txtRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(25, 25, 25)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabPane.addTab("Employee", jPanel2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(43, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(tabPane)
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbDepatureTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDepatureTimeActionPerformed
        
    }//GEN-LAST:event_cbDepatureTimeActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        
    }//GEN-LAST:event_btnSelectActionPerformed

    private void cbPhonenumberEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPhonenumberEActionPerformed
        
    }//GEN-LAST:event_cbPhonenumberEActionPerformed

    private void txtRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoleActionPerformed
        
    }//GEN-LAST:event_txtRoleActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrainRideForm().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelect;
    private javax.swing.JButton btnUpdateEmployee;
    private javax.swing.JComboBox<String> cbClass1Seats;
    private javax.swing.JComboBox<String> cbDepatureTime;
    private javax.swing.JComboBox<String> cbIdbranchE;
    private javax.swing.JTextField cbPhonenumberE;
    private javax.swing.JComboBox<String> cbTrainStationClass;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTable tblTrainRide;
    private javax.swing.JTextField txtFullnameE;
    private javax.swing.JTextField txtIdE;
    private javax.swing.JTextField txtRole;
    // End of variables declaration//GEN-END:variables

    
}
