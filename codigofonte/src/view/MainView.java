package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;

public class MainView {

    private JFrame frmMenuPrincipal;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainView window = new MainView();
                window.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainView() {
        initialize();
    }

    private void initialize() {
        frmMenuPrincipal = new JFrame();
        frmMenuPrincipal.setTitle("Menu Principal");
        frmMenuPrincipal.setBounds(100, 100, 334, 265);
        frmMenuPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMenuPrincipal.getContentPane().setLayout(null);
        
        JButton btnComprarIngresso = new JButton("Comprar Ingresso");
        btnComprarIngresso.addActionListener(e -> {
            IngressoView ingressoView = new IngressoView();
            ingressoView.frame.setVisible(true);
            frmMenuPrincipal.dispose();
        });
        btnComprarIngresso.setBounds(80, 57, 158, 23);
        frmMenuPrincipal.getContentPane().add(btnComprarIngresso);
        
        JButton btnBuscarReserva = new JButton("Buscar Ingresso");
        btnBuscarReserva.addActionListener(e -> {
            BuscarReservaView buscarReservaView = new BuscarReservaView();
            buscarReservaView.getFrame().setVisible(true);
            frmMenuPrincipal.dispose();
        });
        btnBuscarReserva.setBounds(80, 91, 158, 23);
        frmMenuPrincipal.getContentPane().add(btnBuscarReserva);
        
        JButton btnFazerManutencao = new JButton("Fazer Manutenção");
        btnFazerManutencao.addActionListener(e -> {
             LimpezaEManutencaoView limpezaView = new LimpezaEManutencaoView();
             limpezaView.getFrame().setVisible(true);
             frmMenuPrincipal.dispose();
        });
        btnFazerManutencao.setBounds(80, 125, 158, 23);
        frmMenuPrincipal.getContentPane().add(btnFazerManutencao);
    }
    
    public void open() {
        frmMenuPrincipal.setVisible(true);
    }
}