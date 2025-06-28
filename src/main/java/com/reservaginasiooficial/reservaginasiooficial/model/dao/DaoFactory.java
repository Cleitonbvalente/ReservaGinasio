package com.reservaginasiooficial.reservaginasiooficial.model.dao;
import com.reservaginasiooficial.reservaginasiooficial.db.DB;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.impl.ReservaDAOJDBC;
import com.reservaginasiooficial.reservaginasiooficial.model.dao.impl.UsuarioDAOJDBC;


public class DaoFactory {
    public static UsuarioDAO createUsuarioDAO() {
        return new UsuarioDAOJDBC(DB.getConnection());
    }

    public static ReservaDAO createReservaDAO() {
        return new ReservaDAOJDBC(DB.getConnection());
    }
}
