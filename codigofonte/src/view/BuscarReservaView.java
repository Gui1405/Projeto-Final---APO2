package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.IngressoController;
import model.Reserva;
import model.Ingresso;

public class BuscarReservaView {

    private JFrame frmBuscarReserva;
    private JTextField textFieldCodReserva;
    private IngressoController controller;

    public BuscarReservaView() {
        controller = new IngressoController();
        initialize();
    }

    public JFrame getFrame() {
        return frmBuscarReserva;
    }

    private void initialize() {
        frmBuscarReserva = new JFrame();
        frmBuscarReserva.setTitle("Buscar Reserva");
        frmBuscarReserva.setBounds(100, 100, 450, 300);
        frmBuscarReserva.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmBuscarReserva.getContentPane().setLayout(null);
        
        JButton btnVoltarMenu = new JButton("Voltar ao Menu");
        btnVoltarMenu.setBounds(212, 126, 120, 30);
        btnVoltarMenu.addActionListener(e -> {
            frmBuscarReserva.dispose(); 
            MainView menu = new MainView();
            menu.open(); 
        });
        frmBuscarReserva.getContentPane().add(btnVoltarMenu);

        JLabel lblCodReserva = new JLabel("Código da Reserva:");
        lblCodReserva.setBounds(68, 72, 130, 20);
        frmBuscarReserva.getContentPane().add(lblCodReserva);

        textFieldCodReserva = new JTextField();
        textFieldCodReserva.setBounds(212, 70, 120, 25);
        frmBuscarReserva.getContentPane().add(textFieldCodReserva);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(83, 126, 120, 30);
        btnBuscar.addActionListener(e -> buscar());
        frmBuscarReserva.getContentPane().add(btnBuscar);
    }
    
    private void buscar() {
        try {
            String texto = textFieldCodReserva.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(frmBuscarReserva, "Digite o código.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int reservaSelecionada = Integer.parseInt(texto);
            Ingresso filtro = new Ingresso();
            filtro.setId(reservaSelecionada);
            
            Reserva reserva = controller.buscarReserva(filtro);
            
            if (reserva != null) {
                CancelarReservaView cancelar = new CancelarReservaView(reserva.getId());
                cancelar.getFrame().setVisible(true);
                frmBuscarReserva.dispose();
            } else {
                JOptionPane.showMessageDialog(frmBuscarReserva, "Reserva não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frmBuscarReserva, "Digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}