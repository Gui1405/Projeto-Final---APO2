package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import controller.IngressoController;
import model.Ingresso;

public class CancelarReservaView {

    private JFrame frmCancelarReserva;
    private Integer reserva;
    private IngressoController controller;

    public CancelarReservaView(Integer reserva) {
        this.reserva = reserva;
        this.controller = new IngressoController();
        initialize();
    }

    public JFrame getFrame() {
        return frmCancelarReserva;
    }

    private void initialize() {
        frmCancelarReserva = new JFrame();
        frmCancelarReserva.setTitle("Cancelar Reserva");
        frmCancelarReserva.setBounds(100, 100, 450, 300);
        frmCancelarReserva.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmCancelarReserva.getContentPane().setLayout(null);
        
        JLabel lblInfo = new JLabel("Cancelar reserva: " + reserva);
        lblInfo.setBounds(107, 65, 250, 20);
        frmCancelarReserva.getContentPane().add(lblInfo);

        JButton btnVoltarMenu = new JButton("Voltar ao Menu");
        btnVoltarMenu.setBounds(147, 160, 140, 30);
        btnVoltarMenu.addActionListener(e -> {
            MainView menu = new MainView();
            menu.open(); 
            frmCancelarReserva.dispose();
        });
        frmCancelarReserva.getContentPane().add(btnVoltarMenu);

        JButton btnCancelarReserva = new JButton("Cancelar");
        btnCancelarReserva.setBounds(147, 115, 140, 30);
        btnCancelarReserva.addActionListener(e -> realizarCancelamento());
        frmCancelarReserva.getContentPane().add(btnCancelarReserva);
    }
    
    private void realizarCancelamento() {
        int confirm = JOptionPane.showConfirmDialog(frmCancelarReserva,
                "Deseja realmente cancelar a reserva " + reserva + "?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            
            //Empacota o ID num objeto Ingresso
            Ingresso filtro = new Ingresso();
            filtro.setId(reserva);

            String msg = controller.cancelarReserva(filtro);
            
            JOptionPane.showMessageDialog(frmCancelarReserva, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
            
            frmCancelarReserva.dispose();
            new MainView().open();
        }
    }
}