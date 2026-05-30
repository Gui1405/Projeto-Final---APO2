package controller;

import java.sql.SQLException;
import java.util.List;
import banco.ClienteDAO;
import banco.IngressoDAO;
import banco.PoltronaDAO;
import model.Cliente;
import model.Ingresso;
import model.Poltrona;
import model.Reserva;
import model.Sessao;
import model.Sala;

public class IngressoController {

    private ClienteDAO clienteDAO;
    private IngressoDAO ingressoDAO;
    private PoltronaDAO poltronaDAO;

    public IngressoController() {
        this.clienteDAO = new ClienteDAO();
        this.ingressoDAO = new IngressoDAO();
        this.poltronaDAO = new PoltronaDAO();
    }

    //Retorna o objeto Ingresso completo (com ID preenchido) após a compra.
    
    public Ingresso comprarIngresso(Cliente cliente, Sessao sessao, Poltrona poltrona) {
        try {
            if (cliente == null || sessao == null || poltrona == null) {
                return null;
            }

            // O DAO agora retorna o Objeto Cliente atualizado com o ID
            Cliente clienteAtualizado = clienteDAO.salvarOuBuscar(cliente);
            
            if (clienteAtualizado == null || clienteAtualizado.getId() == null) {
                return null;
            }

            Ingresso ingresso = new Ingresso();
            ingresso.setCliente(clienteAtualizado);   
            ingresso.setSessao(sessao);     
            ingresso.setPoltrona(poltrona); 
            ingresso.setStatus("PAGO");

            // O DAO retorna o Objeto Ingresso atualizado com o novo ID gerado
            return ingressoDAO.inserir(ingresso); 

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public Reserva buscarReserva(Ingresso ingressoAlvo) {
        return ingressoDAO.buscarPorIngresso(ingressoAlvo);
    }

    public String cancelarReserva(Ingresso ingressoAlvo) {
        try {
            Reserva reserva = ingressoDAO.buscarPorIngresso(ingressoAlvo);
            
            if (reserva != null) {
                return ingressoDAO.cancelar(reserva);
            } else {
                return "Reserva não encontrada.";
            }
        } catch (SQLException exception) {
            return "Erro ao cancelar: " + exception.getMessage();
        }
    }

    public List<Poltrona> listarPoltronasPorSala(Sala sala) {
        return poltronaDAO.listarPorSala(sala);
    }
}