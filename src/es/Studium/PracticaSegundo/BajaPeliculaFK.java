package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BajaPeliculaFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameBajaPelicula = new Frame("Baja de Pelicula");
	Label labelMensajeBajaPelicula = new Label("Selecciona la Pelicula y el propietario:");
	Choice choPeliculas = new Choice();
	Choice choPropietarios = new Choice();
	Button botonBorrarPelicula = new Button("Borrar");
	Dialog dialogSeguroPelicula = new Dialog(frameBajaPelicula, "AVISO", true);
	Label labelSeguroPelicula = new Label("�Est� seguro de querer borrar esta pelicula?");
	Button botonSiSeguroPelicula = new Button("S�");
	Button botonNoSeguroPelicula = new Button("No");
	Dialog dialogConfirmacionBajaPelicula = new Dialog(frameBajaPelicula, "Baja Cliente", true);
	Label labelConfirmacionBajaPelicula = new Label("Baja de cliente realizada");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaPeliculaFK(String usuario) 
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();

		frameBajaPelicula.setLayout(new FlowLayout());
		frameBajaPelicula.add(labelMensajeBajaPelicula);
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//SELECCIONAMOS LAS PELICULAS
		sentencia = "SELECT * FROM Propietario";
		try
		{
			//CREAMOS SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choPropietarios.removeAll();
			choPropietarios.add("Seleccionar un propietario");
			while(rs.next())
			{
				//BUSCAMOS LOS DATOS DE LOS PROPIETARIOS
				choPropietarios.add(rs.getInt("idPropietario")
						+"-"+rs.getString("nombrePropietario") +"-"+rs.getString("direccionPropietario")
						+"-"+rs.getString("telefonoPropietario")+"-"+rs.getString("dniPropietario"));
			}
		}

		catch (SQLException sqle)
		{

		}

		bd = new BaseDeDatos();
		connection = bd.conectar();
		//BUSCAMOS EN PELICULAS
		sentencia = "SELECT * FROM Peliculas";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choPeliculas.removeAll();
			choPeliculas.add("Selecciona una Pelicula");
			while(rs.next())
			{
				choPeliculas.add(rs.getInt("idPelicula")
						+"-"+rs.getString("nombrePelicula") +"-"+rs.getString("directorPelicula")
						+"-"+rs.getString("precioPelicula")+"-"+rs.getString("idPropietarioFK1")+"\n");

			}
		}

		catch (SQLException sqle)
		{

		}

		finally
		{	}

		//A�ADIMOS EL FRAME DE DAR DE BAJA AL CLIENTE
		frameBajaPelicula.add(labelMensajeBajaPelicula);
		frameBajaPelicula.add(choPeliculas);
		frameBajaPelicula.add(choPropietarios);
		botonBorrarPelicula.addActionListener(this);
		frameBajaPelicula.add(botonBorrarPelicula);

		frameBajaPelicula.setSize(450,180);
		frameBajaPelicula.setResizable(false);
		frameBajaPelicula.setLocationRelativeTo(null);
		frameBajaPelicula.addWindowListener(this);
		frameBajaPelicula.setVisible(true);

		frameBajaPelicula.setLayout(new FlowLayout());

	}

	@Override
	//FUNCIONALIDAD A LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonBorrarPelicula))
		{
			log.guardar(usuario, "Ha pulsado Borrar Cliente");
			dialogSeguroPelicula.setLayout(new FlowLayout());
			dialogSeguroPelicula.addWindowListener(this);
			dialogSeguroPelicula.setSize(270,100);
			dialogSeguroPelicula.setResizable(false);
			dialogSeguroPelicula.setLocationRelativeTo(null);
			dialogSeguroPelicula.add(labelSeguroPelicula);
			botonSiSeguroPelicula.addActionListener(this);
			dialogSeguroPelicula.add(botonSiSeguroPelicula);
			botonNoSeguroPelicula.addActionListener(this);
			dialogSeguroPelicula.add(botonNoSeguroPelicula);
			dialogSeguroPelicula.setVisible(true);
		}

		else if(evento.getSource().equals(botonNoSeguroPelicula))
		{
			log.guardar(usuario, "Ha pulsado el bot�n NO, ha cancelado el borrado");
			dialogSeguroPelicula.setVisible(false);
		}

		else if(evento.getSource().equals(botonSiSeguroPelicula))
		{
			log.guardar(usuario, "Ha pulsado el bot�n SI, ha borrado la pelicula");
			//CONECTAMOS A LA BASE DE DATOS
			bd = new BaseDeDatos();
			connection = bd.conectar();
			String[] elegido = choPropietarios.getSelectedItem().split("-");
			//SENTENCIA DE BORRADO DENTRO DE LA TABLA CLIENTES
			sentencia = "DELETE FROM propietario WHERE idPropietarioFK1 = "+elegido[0];
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				log.guardar(usuario, sentencia);
				statement.executeUpdate(sentencia);
				labelConfirmacionBajaPelicula.setText("Baja de Pelicula Correcta");
			}
			catch (SQLException sqle)
			{
				log.guardar(usuario, "La Pelicula no puede ser borrado, tiene enlazado alg�n dato que debes borrar primero.");
				labelConfirmacionBajaPelicula.setText("Error en Baja");
			}
			finally
			{
				dialogConfirmacionBajaPelicula.setLayout(new FlowLayout());
				dialogConfirmacionBajaPelicula.addWindowListener(this);
				dialogConfirmacionBajaPelicula.setSize(250,100);
				dialogConfirmacionBajaPelicula.setResizable(false);
				dialogConfirmacionBajaPelicula.setLocationRelativeTo(null);
				dialogConfirmacionBajaPelicula.add(labelConfirmacionBajaPelicula);
				dialogConfirmacionBajaPelicula.setVisible(true);
			}
		}

	}
	public void windowClosing(WindowEvent e) 
	{
		if(frameBajaPelicula.isActive())
		{
			frameBajaPelicula.setVisible(false);
		}
		else if(dialogSeguroPelicula.isActive())
		{
			dialogSeguroPelicula.setVisible(false);
		}
		else if(dialogConfirmacionBajaPelicula.isActive())
		{
			dialogConfirmacionBajaPelicula.setVisible(false);
			dialogSeguroPelicula.setVisible(false);
			frameBajaPelicula.setVisible(false);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
