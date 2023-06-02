package Liquidaciones;

import Clases.VerificarFrames;
import Conexion.ClsConexionLocal;

import Entidad.ClsEntidadLiquidacion;
import Negocio.ClsCliente;
import Negocio.ClsCompraVentaVisorDocumento;
import Negocio.ClsCuentaPorCobrar;
import Presentacion.FrmGastosEfectivoLiquidacion;
import Presentacion.FrmPrincipal;
import static Presentacion.FrmPrincipal.Escritorio;
import ds.desktop.notify.DesktopNotify;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public final class FrmLiquidacionesVisorAsignados extends javax.swing.JInternalFrame {
     private final Connection connection = new ClsConexionLocal().getConection();

    private static FrmLiquidacionesVisorAsignados frmLiquidacionesVisorVentaEmitida;
    static String IdEmpleado;
    public static String idcaja, total, cargo, abono, estado;

    public static FrmLiquidacionesVisorAsignados getInstancia() {
        if (frmLiquidacionesVisorVentaEmitida == null) {
            frmLiquidacionesVisorVentaEmitida = new FrmLiquidacionesVisorAsignados();
        }
        return frmLiquidacionesVisorVentaEmitida;
    }
    private static DefaultTableCellRenderer tcr;

    static ResultSet rs = null;

    public static DefaultTableModel dtm = new DefaultTableModel();
    // DefaultTableModel dtm1 = new DefaultTableModel();
    String id[] = new String[50];
    static int intContador;
    Date fecha_ini, fecha_fin;
    String documento, criterio, busqueda, Total;
    boolean valor = true;
    int n = 0;

    public FrmLiquidacionesVisorAsignados() {
        initComponents();
        String c = FrmVerLiquidacion.idlb.getText();
        txtId.setText(c);
        lbVenta.setVisible(false);
        //lblIdCompra.setVisible(false);
        this.setSize(769, 338);
        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);

        BuscarDocumentos();

        ocultarcolumna();
      

        tabCuenta.setRowHeight(32);

    }

    static void ocultarcolumna() {
//        tabCuenta.getColumnModel().getColumn(1).setMaxWidth(0);
//        tabCuenta.getColumnModel().getColumn(1).setMinWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
//
//        tabCuenta.getColumnModel().getColumn(3).setMaxWidth(0);
//        tabCuenta.getColumnModel().getColumn(3).setMinWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
//
//        tabCuenta.getColumnModel().getColumn(0).setMaxWidth(0);
//        tabCuenta.getColumnModel().getColumn(0).setMinWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
//
//        tabCuenta.getColumnModel().getColumn(10).setMaxWidth(0);
//        tabCuenta.getColumnModel().getColumn(10).setMinWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(10).setMaxWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(10).setMinWidth(0);
//
//        tabCuenta.getColumnModel().getColumn(9).setMaxWidth(0);
//        tabCuenta.getColumnModel().getColumn(9).setMinWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(9).setMaxWidth(0);
//        tabCuenta.getTableHeader().getColumnModel().getColumn(9).setMinWidth(0);
    }

    void actualizaridliquidacion() {
        String c = FrmVerLiquidacion.idlb.getText();
        txtId.setText(c);
    }

    static void diferencia() {
        //String titulos[] = {"ID", "FECHA", "DOCUMENTO", "IDCLIENTE", "CLIENTE", "DIR", "CREDITO", "VENTA", "ABONO", "TIPOVENTA", "TRANSACCION"};
        try {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat formateador = new DecimalFormat("#######.##", simbolos);

            int fila = 0;

            float bit=0,tot = 0, devolucion = 0, valorVenta = 0,
                    abono2 = 0, credito2 = 0, inicial = 0, sfinal = 0, abono = 0, credito = 0, efectivo = 0, entrada = 0, salida = 0, tarjeta = 0, Moneda = 0, cheque = 0;
            fila = tabCuenta.getRowCount();
            for (int f = 0; f < fila; f++) {
                String dato = String.valueOf(tabCuenta.getValueAt(f, 1));
                if (dato.equals("Entrada")) {
                    entrada += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Moneda")) {
                    Moneda += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Devolucion")) {
                    devolucion += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Moneda Digital")) {
                    bit += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                
                 if (dato.equals("Efectivo")) {
                    efectivo += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Salida")) {
                    salida += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Pago Con Cheque")) {
                    cheque += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

                }
                if (dato.equals("Pago con Tarjeta")) {
                    tarjeta += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));
                    System.out.println(tarjeta);

                }
 tot += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));
                inicial = Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 3)));
                abono2 = Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 4)));
                credito2 = Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 7)));
                sfinal = inicial - abono2 + credito2;
                tabCuenta.setValueAt(formateador.format(sfinal), f, 8);

                abono += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 4)));

                credito += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 7)));
                valorVenta += Float.parseFloat(String.valueOf(tabCuenta.getModel().getValueAt(f, 5)));

            }
           
            valorVenta = valorVenta - efectivo-bit - Moneda - devolucion - salida - entrada - tarjeta - cheque;
             tot =-valorVenta + devolucion - entrada + salida + efectivo+bit + Moneda + cheque + tarjeta - abono + credito;

            lbDiferencia.setText(String.valueOf(formateador.format(tot)));
            txtventa.setText(String.valueOf(formateador.format(valorVenta)));
            txtmoneda.setText(String.valueOf(formateador.format(Moneda)));
            txttarjeta.setText(String.valueOf(formateador.format(tarjeta)));
            txtcheque.setText(String.valueOf(formateador.format(cheque)));

            txtsalida.setText(String.valueOf(formateador.format(salida)));
            txtcargo.setText(String.valueOf(formateador.format(credito)));
            txtabono.setText(String.valueOf(formateador.format(abono)));
            txtefectivo.setText(String.valueOf(formateador.format(efectivo)));
              lbbitcoin.setText(String.valueOf(formateador.format(bit)));
            lbDiferencia.setText(String.valueOf(formateador.format(tot)));
            txtdevolucion.setText(String.valueOf(formateador.format(devolucion)));

        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    

    public static void BuscarDocumentos() {
        String titulos[] = {"Id", "Cliente", "Direccion", "Saldo Inicial", "Abono", "Venta", "Contado", "Credito", "Saldo Final"};
        ClsCompraVentaVisorDocumento compra = new ClsCompraVentaVisorDocumento();
        ArrayList<ClsEntidadLiquidacion> productoCompras = compra.listarDocumentosLiquidacion(Integer.parseInt(txtId.getText()));
        DefaultTableModel defaultTableModel = new DefaultTableModel(null, titulos);
        Iterator iterator = productoCompras.iterator();
        String fila[] = new String[9];
        while (iterator.hasNext()) {
            ClsEntidadLiquidacion Liquidacion;
            Liquidacion = (ClsEntidadLiquidacion) iterator.next();
            fila[0] = Liquidacion.getStrIdCliente();
            fila[1] = Liquidacion.getStrNOmbre();
            fila[2] = Liquidacion.getStrDireccion();
            fila[3] = Liquidacion.getStrSaldoInicial();
            fila[4] = Liquidacion.getStrAbono();
            fila[5] = Liquidacion.getStrTotalPagar();
            fila[6] = Liquidacion.getStrSaldo();
            fila[7] = Liquidacion.getStrLlimite();
            fila[8] = Liquidacion.getStrSaldoActual();

            // fila[8] = Liquidacion.getStrSaldo();
            defaultTableModel.addRow(fila);

        }
        tabCuenta.setModel(defaultTableModel);
        diferencia();
        CrearTablaDetalleProducto();
        ActualizarDiferencia();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtvendedor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtrepartidor = new javax.swing.JLabel();
        txtRuta = new javax.swing.JLabel();
        txtId = new javax.swing.JLabel();
        lbfecha = new javax.swing.JLabel();
        lbiterador = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabCuenta = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbVenta = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lbbitcoin = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbdiferencia = new javax.swing.JLabel();
        txtsalida = new javax.swing.JLabel();
        txtdevolucion = new javax.swing.JLabel();
        txtabono = new javax.swing.JLabel();
        txtcargo = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtcheque = new javax.swing.JLabel();
        txttarjeta = new javax.swing.JLabel();
        txtmoneda = new javax.swing.JLabel();
        txtefectivo = new javax.swing.JLabel();
        txtventa = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbDiferencia = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mCompra = new javax.swing.JMenuItem();
        mCompra1 = new javax.swing.JMenuItem();
        mCompra2 = new javax.swing.JMenuItem();
        mnbit = new javax.swing.JMenuItem();
        mCompra3 = new javax.swing.JMenuItem();
        mCompra4 = new javax.swing.JMenuItem();
        mCompra5 = new javax.swing.JMenuItem();
        mCompra6 = new javax.swing.JMenuItem();
        mCompra17 = new javax.swing.JMenuItem();
        mCompra22 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mCompra7 = new javax.swing.JMenuItem();
        mCompra8 = new javax.swing.JMenuItem();
        mCompra9 = new javax.swing.JMenuItem();
        mCompra12 = new javax.swing.JMenuItem();
        mCompra13 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mCompra11 = new javax.swing.JMenuItem();
        mCompra14 = new javax.swing.JMenuItem();
        mCompra16 = new javax.swing.JMenuItem();
        mCompra15 = new javax.swing.JMenuItem();
        mCompra19 = new javax.swing.JMenuItem();
        mCompra18 = new javax.swing.JMenuItem();
        mCompra20 = new javax.swing.JMenuItem();
        mCompra10 = new javax.swing.JMenuItem();
        mCompra21 = new javax.swing.JMenuItem();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Informe De Documentos  ");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(223, 223, 223));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Liquidacion"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Ruta:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 70, -1));

        txtvendedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtvendedor.setText("jLabel2");
        jPanel1.add(txtvendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Vendedor :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Repartidor:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 90, -1));

        txtrepartidor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtrepartidor.setText("jLabel5");
        jPanel1.add(txtrepartidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, -1, -1));

        txtRuta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtRuta.setText("jLabel6");
        jPanel1.add(txtRuta, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 350, -1));

        txtId.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtId.setText("jLabel7");
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        lbfecha.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbfecha.setText("jLabel2");
        jPanel1.add(lbfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 0, 0));

        tabCuenta = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {

                if(colIndex ==6){
                    return false;
                }
                if(colIndex ==8){
                    return false;
                }
                else{
                    return false;} //Disallow the editing of any cell
            }
        };
        tabCuenta.setAutoCreateRowSorter(true);
        tabCuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabCuenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Cliente", "Saldo Inicial", "Abono", "Venta", "Contado", "Credito", "Saldo Final"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabCuenta.setRowHeight(40);
        tabCuenta.setShowGrid(true);
        tabCuenta.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tabCuentaMouseMoved(evt);
            }
        });
        tabCuenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabCuentaFocusGained(evt);
            }
        });
        tabCuenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabCuentaMouseClicked(evt);
            }
        });
        tabCuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabCuentaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabCuentaKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tabCuenta);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbVenta.setText("jLabel5");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("Diferencia");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 90, 70));

        lbbitcoin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbbitcoin.setText("0.00");
        jPanel3.add(lbbitcoin, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Total Diferencias:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, -1, -1));

        lbdiferencia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbdiferencia.setText("0.00");
        jPanel3.add(lbdiferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, -1, -1));

        txtsalida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtsalida.setText("0.00");
        jPanel3.add(txtsalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, -1, -1));

        txtdevolucion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtdevolucion.setText("0.00");
        jPanel3.add(txtdevolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, -1, -1));

        txtabono.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtabono.setText("0.00");
        jPanel3.add(txtabono, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        txtcargo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtcargo.setText("0.00");
        jPanel3.add(txtcargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setText("Total Creditos:");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setText("Total Abono:");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, -1, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Total Devolucion:");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Total Salida:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, -1, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Total Venta:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Total Efectivo:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Total Moneda:");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Total Tarjeta:");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Total Cheque");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        txtcheque.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtcheque.setText("0.00");
        jPanel3.add(txtcheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, -1));

        txttarjeta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txttarjeta.setText("0.00");
        jPanel3.add(txttarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, -1, -1));

        txtmoneda.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtmoneda.setText("0.00");
        jPanel3.add(txtmoneda, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));

        txtefectivo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtefectivo.setText("0.00");
        jPanel3.add(txtefectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        txtventa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtventa.setText("0.00");
        jPanel3.add(txtventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Total Bitcoin:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, -1, -1));

        lbDiferencia.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lbDiferencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDiferencia.setText("0.00");
        jPanel3.add(lbDiferencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 220, 80));

        jMenu1.setText("Procesos");

        mCompra.setText("Efectivo");
        mCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompraActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra);

        mCompra1.setText("Moneda");
        mCompra1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra1ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra1);

        mCompra2.setText("Tarjeta");
        mCompra2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra2ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra2);

        mnbit.setText("Bitcoin");
        mnbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnbitActionPerformed(evt);
            }
        });
        jMenu1.add(mnbit);

        mCompra3.setText("Cheque");
        mCompra3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra3ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra3);

        mCompra4.setText("Gasto");
        mCompra4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra4ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra4);

        mCompra5.setText("Ingreso");
        mCompra5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra5ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra5);

        mCompra6.setText("Devolucion");
        mCompra6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra6ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra6);

        mCompra17.setText("Eliminar");
        mCompra17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra17ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra17);

        mCompra22.setText("Actualizar");
        mCompra22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra22ActionPerformed(evt);
            }
        });
        jMenu1.add(mCompra22);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Calculos");

        mCompra7.setText("Actualizar Saldos");
        mCompra7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra7ActionPerformed(evt);
            }
        });
        jMenu3.add(mCompra7);

        mCompra8.setText("Agregar Abono");
        mCompra8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra8ActionPerformed(evt);
            }
        });
        jMenu3.add(mCompra8);

        mCompra9.setText("Agregar Credito");
        mCompra9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra9ActionPerformed(evt);
            }
        });
        jMenu3.add(mCompra9);

        mCompra12.setText("Anular Abono");
        mCompra12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra12ActionPerformed(evt);
            }
        });
        jMenu3.add(mCompra12);

        mCompra13.setText("Anular Credito");
        mCompra13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra13ActionPerformed(evt);
            }
        });
        jMenu3.add(mCompra13);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Informes");

        mCompra11.setText("Informe de Documentos");
        mCompra11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra11ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra11);

        mCompra14.setText("Saldos Por Cliente");
        mCompra14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra14ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra14);

        mCompra16.setText("Cuadratura de Ruta");
        mCompra16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra16ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra16);

        mCompra15.setText("Hoja de Carga");
        mCompra15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra15ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra15);

        mCompra19.setText("Imprimir Saldo Actual");
        mCompra19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra19ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra19);

        mCompra18.setText("Imprimir Saldo Inicial");
        mCompra18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra18ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra18);

        mCompra20.setText("Imprimir Historial");
        mCompra20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra20ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra20);

        mCompra10.setText("Ver Doc x Cliente");
        mCompra10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra10ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra10);

        mCompra21.setText("Saldos Ticket");
        mCompra21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompra21ActionPerformed(evt);
            }
        });
        jMenu2.add(mCompra21);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(lbVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(633, 633, 633)
                        .addComponent(lbiterador, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(lbVenta))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(lbiterador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );

        jPanel1.getAccessibleContext().setAccessibleName("Opciones de busqueda y");

        getAccessibleContext().setAccessibleName("Informe De Documentos Emitidos");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    static void CrearTablaDetalleProducto() {
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0 || column == 3 || column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {

                    l.setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }
                if (isSelected) {
                    l.setBackground(new Color(153, 204, 255));
                    l.setForeground(Color.BLACK);
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        l.setBackground(new Color(229, 246, 245));
                    }
                }
                return l;
            }
        };
        for (int i = 0; i < tabCuenta.getColumnCount(); i++) {
            tabCuenta.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        //tabCuenta.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        int[] anchos = {10, 100, 100, 40, 40, 40, 40, 40, 40};
        for (int i = 0; i < tabCuenta.getColumnCount(); i++) {
            tabCuenta.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        ocultarcolumna();

    }


    private void tabCuentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCuentaMouseClicked


    }//GEN-LAST:event_tabCuentaMouseClicked

    void imprimir() {
        Connection connection = new ClsConexionLocal().getConection();
        Map p = new HashMap();
        p.put("pcriterio", Integer.parseInt(txtId.getText()));
         p.put("pruta", txtRuta.getText());
          p.put("pvendedor", txtvendedor.getText());
           p.put("prepartidor", txtrepartidor.getText());

        JasperReport report;
        JasperPrint print;
        
        
        
        
         try {
                    report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptPickingListTicket.jrxml");
                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
        
        

       
        
      
        
         try {
                    report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptPickingListTicketSum.jrxml");
                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
        

    }

    void imprimirDoc() {
        Connection connection = new ClsConexionLocal().getConection();
        Map p = new HashMap();
        p.put("pcriterio", Integer.parseInt(txtId.getText()));
        p.put("pruta", (txtRuta.getText()));
        p.put("pvendedor", (txtvendedor.getText()));
        p.put("prepartidor", (txtrepartidor.getText()));

        JasperReport report;
        JasperPrint print;
        try {
                     report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptInformeDocumentos.jrxml");
                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
       try {
                     report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptRev.jrxml");
                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
        
         
        
         
         
          

    }
    
    void imprimircuadre() throws ParseException {
        Connection connection = new ClsConexionLocal().getConection();
        Map p = new HashMap();
         SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
         fecha = formatoDeFecha.parse(lbfecha.getText());
        p.put("pCriterio", fecha);
          p.put("dir", (new File("").getAbsolutePath() + "/src/Reportes/"));

        JasperReport report;
        JasperPrint print;
        
        try {
           
              report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptCuadreLiquidacionTicket2.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Informe de Liquidacion");
            view.setVisible(true);
        } catch (JRException e) {
            System.err.println(e);
        }
        try {
           
              report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptCuadreLiquidacionTicket.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Informe de Liquidacion");
            view.setVisible(true);
        } catch (JRException e) {
            System.err.println(e);
        }
        
        

    }

    
    


    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        frmLiquidacionesVisorVentaEmitida = null;
    }//GEN-LAST:event_formInternalFrameClosed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased


    }//GEN-LAST:event_formKeyReleased

    private void tabCuentaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCuentaMouseMoved

    }//GEN-LAST:event_tabCuentaMouseMoved

    public static void ActualizarDiferencia() {
        Connection connection = new ClsConexionLocal().getConection();
        try {
            String sql = "UPDATE `liquidacion` SET `li_dif`='" + lbDiferencia.getText() + "' WHERE `idliquidacion`='" + txtId.getText() + "';";
            PreparedStatement sts = connection.prepareStatement(sql);
            sts.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void tabCuentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabCuentaKeyReleased

        //  CuentaPorCobraretallado();
        //  calcular();

    }//GEN-LAST:event_tabCuentaKeyReleased
    public static void calcular() {
        try {
            BuscarDocumentos();
            diferencia();
            ActualizarDiferencia();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    private void tabCuentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabCuentaFocusGained

    }//GEN-LAST:event_tabCuentaFocusGained

    private void tabCuentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabCuentaKeyPressed

//        Connection connection = new ClsConexionLocal().getConection();
//        int fila = tabCuenta.getSelectedRow();
//        String venta = ((String) tabCuenta.getValueAt(fila, 7));
//        String dato = (((String) tabCuenta.getValueAt(fila, 10)));
//        String id = (((String) tabCuenta.getValueAt(fila, 0)));
//        System.out.println(id);
//        if (dato.equals("VENTA") && (!venta.equals("0.00"))) {
//            JOptionPane.showMessageDialog(null, "Dato no modificable");
//        } else {
//            try {
//                String sql = "DELETE FROM `gastosvarios` WHERE `idcorrelativo`='" + id + "';";
//                PreparedStatement sts = connection.prepareStatement(sql);
//                sts.executeUpdate();
//
//                //  BuscarGastos();
//                // btnCalc.doClick();
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, ex);
//            }
//
//            try {
//                if (venta.equals("0.00")) {
//                    String sql = "DELETE FROM `venta` WHERE `venta_idcaja`='" + id + "';";
//                    PreparedStatement sts = connection.prepareStatement(sql);
//                    sts.executeUpdate();
//
//                }
//            } catch (Exception e) {
//            }
//        }
//        BuscarDocumentos();
//        calcular();
//
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            try {
                int row = tabCuenta.getSelectedRow();
                String Ids = (String) tabCuenta.getValueAt(row, 0);
                String Name = (String) tabCuenta.getValueAt(row,1);
                String direccion = (String) tabCuenta.getValueAt(row, 2);
                VerificarFrames.abrirSmallFrames(Actualizacion.FrmActualizarCliente.getInstancia());
                Actualizacion.FrmActualizarCliente.txtIdCliente.setText(Ids);
                Actualizacion.FrmActualizarCliente.txtNombre.setText(Name);
                Actualizacion.FrmActualizarCliente.txtDir.setText(direccion);
                Actualizacion.FrmActualizarCliente.txtDir.requestFocus();
                
            } catch (Exception e) {
            }

        }
    }//GEN-LAST:event_tabCuentaKeyPressed

    private void mCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompraActionPerformed

        FrmGastosEfectivoLiquidacion gasto = new FrmGastosEfectivoLiquidacion();
        gasto.setVisible(true);
    }//GEN-LAST:event_mCompraActionPerformed

    private void mCompra1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra1ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(4);

        gastos.cbTipoTransaccion.setEnabled(false);
        gastos.txtValorGasto.grabFocus();

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mCompra1ActionPerformed

    private void mCompra2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra2ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(2);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mCompra2ActionPerformed

    private void mCompra3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra3ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(5);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mCompra3ActionPerformed

    private void mCompra4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra4ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(1);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();

    }//GEN-LAST:event_mCompra4ActionPerformed

    private void mCompra5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra5ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(0);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mCompra5ActionPerformed

    private void mCompra6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra6ActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(12);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mCompra6ActionPerformed

    private void mCompra7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra7ActionPerformed
      
        
         ClsCliente vergas = new ClsCliente();
         vergas.EstimiarSaldoIni(Integer.parseInt(txtId.getText()));
        
        try {
           
            int fila = tabCuenta.getRowCount();
            String idcliente;

            //
//            connection.close();
            int idliquidacion = Integer.parseInt(txtId.getText());

            for (int i = 0; i < fila; i++) {
                idcliente = String.valueOf(tabCuenta.getValueAt(i, 0));

                System.out.println(idcliente + " " + idliquidacion
                );
                vergas.EstimarSaldos(idcliente, idliquidacion);

            }
            BuscarDocumentos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }//GEN-LAST:event_mCompra7ActionPerformed

    private void mCompra8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra8ActionPerformed
         int fila=tabCuenta.getSelectedRow();
        VerificarFrames.abrirSmallFrames(FrmAgregarAbonoLiq.getInstancia());
        FrmAgregarAbonoLiq.jLabel1.setText("ABONO");
        FrmAgregarAbonoLiq.txtAbono.setText(String.valueOf(tabCuenta.getValueAt(fila, 3)));
    }//GEN-LAST:event_mCompra8ActionPerformed

    private void mCompra9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra9ActionPerformed
       int fila=tabCuenta.getSelectedRow();
        VerificarFrames.abrirSmallFrames(FrmAgregarAbonoLiq.getInstancia());
        FrmAgregarAbonoLiq.jLabel1.setText("CARGO");
        FrmAgregarAbonoLiq.txtAbono.setText(String.valueOf(tabCuenta.getValueAt(fila, 5)));
    }//GEN-LAST:event_mCompra9ActionPerformed

    private void mCompra10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra10ActionPerformed
        try {
            VerificarFrames.abrirSmallFrames(FrmDocumentosPorCliente.getInstancia());

        } catch (Exception e) {
        }
    }//GEN-LAST:event_mCompra10ActionPerformed

    private void mCompra11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra11ActionPerformed
        imprimirDoc();
    }//GEN-LAST:event_mCompra11ActionPerformed

    private void mCompra12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra12ActionPerformed
      ClsCuentaPorCobrar cuenta= new ClsCuentaPorCobrar();
      int liquidacion=Integer.parseInt(txtId.getText());
      int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
      cuenta.AnularAbonoLiq(liquidacion, cliente);
    }//GEN-LAST:event_mCompra12ActionPerformed

    private void mCompra13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra13ActionPerformed
      ClsCuentaPorCobrar cuenta= new ClsCuentaPorCobrar();
      int liquidacion=Integer.parseInt(txtId.getText());
      int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
      cuenta.AnularcreditoLiq(liquidacion, cliente);
    }//GEN-LAST:event_mCompra13ActionPerformed

    private void mCompra14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra14ActionPerformed
     
      
      int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
        Map p = new HashMap();

        p.put("pidcliente", cliente);
         p.put("pidliquidacion", Integer.parseInt(txtId.getText()));

        JasperReport report;
        JasperPrint print;


        
        
         try {
                    report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptSaldosCliente.jrxml");
                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
      
     
    }//GEN-LAST:event_mCompra14ActionPerformed

    private void mCompra15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra15ActionPerformed
      imprimir();
    }//GEN-LAST:event_mCompra15ActionPerformed

    private void mCompra16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra16ActionPerformed
         try {
             imprimircuadre();
         } catch (ParseException ex) {
             Logger.getLogger(FrmLiquidacionesVisorAsignados.class.getName()).log(Level.SEVERE, null, ex);
         }
    }//GEN-LAST:event_mCompra16ActionPerformed

    private void mCompra17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra17ActionPerformed
             Connection connection = new ClsConexionLocal().getConection();
        int fila = tabCuenta.getSelectedRow();
      
        String id = (((String) tabCuenta.getValueAt(fila, 0)));
        System.out.println(id);
        
            try {
                String sql = "DELETE FROM `gastosvarios` WHERE `idcorrelativo`='" + id + "';";
                PreparedStatement sts = connection.prepareStatement(sql);
                sts.execute();
                 DesktopNotify.showDesktopMessage("", " " + "GASTO ELIMINADO", DesktopNotify.INFORMATION, 5000);
                 BuscarDocumentos();
                

               
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        
    }//GEN-LAST:event_mCompra17ActionPerformed

    private void mCompra18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra18ActionPerformed
       int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
        Map p = new HashMap();

        p.put("pidcliente", cliente);
         p.put("pidliquidacion", Integer.parseInt(txtId.getText()));

        JasperReport report;
        JasperPrint print;
        
        
        try {
                     report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptSaldosClienteinicial.jrxml");

                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
        
        
      
    }//GEN-LAST:event_mCompra18ActionPerformed

    private void mCompra19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra19ActionPerformed
        int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      String saldo=String.valueOf(tabCuenta.getValueAt(fila, 8));
      
        Map p = new HashMap();

        p.put("pidcliente", cliente);
         p.put("pidliquidacion", Integer.parseInt(txtId.getText()));
          p.put("sactual", saldo);

        JasperReport report;
        JasperPrint print;
        
        
        
          try {
                     report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptSaldosClienteSActual.jrxml");

                    print = JasperFillManager.fillReport(report, p, connection);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException r) {
                    System.err.println(r);
                }
    }//GEN-LAST:event_mCompra19ActionPerformed

    private void mCompra20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra20ActionPerformed
      int fila=tabCuenta.getSelectedRow();
         String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
        String s1,abono,cre,sfinal;
      s1=String.valueOf(tabCuenta.getValueAt(fila, 3));
      abono=String.valueOf(tabCuenta.getValueAt(fila, 4));
      cre=String.valueOf(tabCuenta.getValueAt(fila, 7));
      sfinal=String.valueOf(tabCuenta.getValueAt(fila, 8));
      
      
       Map p = new HashMap();

        p.put("pidcliente", cliente);
         p.put("pidliquidacion", Integer.parseInt(txtId.getText()));
          p.put("sactual", s1);
           p.put("abono", Float.parseFloat(abono));
            p.put("cre", Float.parseFloat(cre));
             p.put("sfinal", sfinal);

        JasperReport report;
        JasperPrint print;
        try {

            report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptSaldosClienteSActualH.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Control De Credito");
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }//GEN-LAST:event_mCompra20ActionPerformed

    private void mnbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnbitActionPerformed
        FrmGastosLiquidacion gastos = new FrmGastosLiquidacion();
        Escritorio.add(gastos);
        Dimension desktopSize = Escritorio.getSize();
        Dimension FrameSize = gastos.getSize();
        gastos.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
        gastos.IdCajaGastosVarios = FrmPrincipal.lbidcaja.getText();
        gastos.accion = "Nuevo";
        gastos.valido = "si";
        gastos.IdLiquidacion.setText(txtId.getText());
        gastos.IdLiquidacion.setVisible(false);
        gastos.cbTipoTransaccion.setSelectedIndex(13);
        gastos.txtValorGasto.grabFocus();
        gastos.cbTipoTransaccion.setEnabled(false);

        gastos.show();
        gastos.txtValorGasto.grabFocus();
    }//GEN-LAST:event_mnbitActionPerformed

    private void mCompra21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra21ActionPerformed
 
      int fila=tabCuenta.getSelectedRow();
      String cliente=String.valueOf(tabCuenta.getValueAt(fila, 0));
      
        Map p = new HashMap();

        p.put("pidcliente", cliente);
         p.put("pidliquidacion", Integer.parseInt(txtId.getText()));

        JasperReport report;
        JasperPrint print;
        try {

            report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptSaldosClienteticket.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Control De Credito");
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println(e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_mCompra21ActionPerformed

    private void mCompra22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompra22ActionPerformed
       BuscarDocumentos();
    }//GEN-LAST:event_mCompra22ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane5;
    private static javax.swing.JLabel lbDiferencia;
    public static javax.swing.JLabel lbVenta;
    private static javax.swing.JLabel lbbitcoin;
    private static javax.swing.JLabel lbdiferencia;
    public static javax.swing.JLabel lbfecha;
    private javax.swing.JLabel lbiterador;
    private javax.swing.JMenuItem mCompra;
    private javax.swing.JMenuItem mCompra1;
    private javax.swing.JMenuItem mCompra10;
    private javax.swing.JMenuItem mCompra11;
    private javax.swing.JMenuItem mCompra12;
    private javax.swing.JMenuItem mCompra13;
    private javax.swing.JMenuItem mCompra14;
    private javax.swing.JMenuItem mCompra15;
    private javax.swing.JMenuItem mCompra16;
    private javax.swing.JMenuItem mCompra17;
    private javax.swing.JMenuItem mCompra18;
    private javax.swing.JMenuItem mCompra19;
    private javax.swing.JMenuItem mCompra2;
    private javax.swing.JMenuItem mCompra20;
    private javax.swing.JMenuItem mCompra21;
    private javax.swing.JMenuItem mCompra22;
    private javax.swing.JMenuItem mCompra3;
    private javax.swing.JMenuItem mCompra4;
    private javax.swing.JMenuItem mCompra5;
    private javax.swing.JMenuItem mCompra6;
    private javax.swing.JMenuItem mCompra7;
    private javax.swing.JMenuItem mCompra8;
    private javax.swing.JMenuItem mCompra9;
    private javax.swing.JMenuItem mnbit;
    public static javax.swing.JTable tabCuenta;
    public static javax.swing.JLabel txtId;
    public static javax.swing.JLabel txtRuta;
    private static javax.swing.JLabel txtabono;
    private static javax.swing.JLabel txtcargo;
    private static javax.swing.JLabel txtcheque;
    private static javax.swing.JLabel txtdevolucion;
    private static javax.swing.JLabel txtefectivo;
    private static javax.swing.JLabel txtmoneda;
    public static javax.swing.JLabel txtrepartidor;
    private static javax.swing.JLabel txtsalida;
    private static javax.swing.JLabel txttarjeta;
    public static javax.swing.JLabel txtvendedor;
    private static javax.swing.JLabel txtventa;
    // End of variables declaration//GEN-END:variables
}
