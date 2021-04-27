package es.Studium.PracticaSegundo;


import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaAlquiler implements WindowListener, ActionListener
{
	Frame ventanaAlquilerConsulta = new Frame("Consulta");
	TextArea listadoAlquilerConsulta = new TextArea(5, 30);
	Button botonPdf = new Button("PDF");
	Label labelMensajeAlquierConsulta = new Label("");

	BaseDeDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaAlquiler()
	{
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT * FROM alquileres";
		
		try
		{
			//CREAMOS LA SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//CREAMOS EL RESULT SET
			rs = statement.executeQuery(sentencia);
			listadoAlquilerConsulta.selectAll();
			listadoAlquilerConsulta.setText("");
			listadoAlquilerConsulta.append("id\tId Clientes\tId Peliculas\n");
			while(rs.next())
			{
				listadoAlquilerConsulta.append(rs.getInt("idAlquiler")
						+"-"+rs.getString("idClientesFK2") +"-"+rs.getString("idPeliculasFK3")+"\n");
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			labelMensajeAlquierConsulta.setText("Error");
		}
		finally
		{
			ventanaAlquilerConsulta.setLayout(new FlowLayout());
			listadoAlquilerConsulta.setEditable(false);
			ventanaAlquilerConsulta.add(listadoAlquilerConsulta);
			ventanaAlquilerConsulta.add(botonPdf);
			ventanaAlquilerConsulta.setSize(250,200);
			ventanaAlquilerConsulta.setResizable(false);
			ventanaAlquilerConsulta.setLocationRelativeTo(null);
			ventanaAlquilerConsulta.addWindowListener(this);
			ventanaAlquilerConsulta.setVisible(true);
			bd.desconectar(connection);
		}
	}

	public void windowClosing(WindowEvent e)
	{
		if(ventanaAlquilerConsulta.isActive())
		{
			ventanaAlquilerConsulta.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent evento) 
	{
		
	}
	public void windowActivated(WindowEvent we) 
	{
		
	}
	public void windowClosed(WindowEvent we) 
	{
		
	}
	public void windowDeactivated(WindowEvent we) 
	{
		
	}
	public void windowDeiconified(WindowEvent we) 
	{
		
	}
	public void windowIconified(WindowEvent we) 
	{
		
	}
	public void windowOpened(WindowEvent we) 
	{
		
	}
}