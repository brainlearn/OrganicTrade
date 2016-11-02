package mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mpoo.bsi.ufrpe.organictrade.Infra.Persistencia.ComandosSql;
import mpoo.bsi.ufrpe.organictrade.Infra.Persistencia.DatabaseHelper;
import mpoo.bsi.ufrpe.organictrade.Infra.Session;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Dominio.User;

public class UsuarioPersistencia {
    private SQLiteDatabase db;
    private DatabaseHelper banco = Session.getDbAtual();

    public User criarUsuario(Cursor cursor) {
        User user = new User();
        user.setId_user(cursor.getString(0));
        user.setUserName(cursor.getString(1));
        user.setPassword(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setName(cursor.getString(4));
        user.setPhone(cursor.getString(5));
        return user;
    }

    public boolean buscarELogarUsuario(String username, String senha) {
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(ComandosSql.sqlUsuarioApartirDoLoginESenha(), new String[]{username,senha});
        if(cursor.moveToFirst()) {
            User user = criarUsuario(cursor);
            logarUsuario(user);
            Session.setUserAtual(user);
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void inserirUsuario(User user){
        db = banco.getWritableDatabase();
        ContentValues valoresUsuario = new ContentValues();
        valoresUsuario.put(DatabaseHelper.getColumnUserId(),user.getId_user());
        valoresUsuario.put(DatabaseHelper.getColumnUserUsername(), user.getUserName());
        valoresUsuario.put(DatabaseHelper.getColumnUserPassword(), user.getPassword());
        valoresUsuario.put(DatabaseHelper.getColumnUserEmail(), user.getEmail());
        valoresUsuario.put(DatabaseHelper.getColumnUserName(), user.getName());
        db.insert(DatabaseHelper.getTableUserName(), null, valoresUsuario);
        db.close();
    }

    public void logarUsuario(User user){
        db = banco.getWritableDatabase();
        ContentValues valoresUsuario = new ContentValues();
        valoresUsuario.put(DatabaseHelper.getColumnUserLoggedId(),user.getId_user());
        db.insert(DatabaseHelper.getTableUserLoggedName(), null, valoresUsuario);
        db.close();
    }

    public boolean usuarioLogado() {
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(ComandosSql.sqlUsuarioLogado(),null);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void deslogarUsuario(){
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(ComandosSql.sqlDeslogarUsuario(), new String[]{Session.getUserAtual().getId_user()});
        if(cursor.moveToFirst()) {
            Session.setUserAtual(null);
            db.execSQL(ComandosSql.sqlLimparTabela());
        }
    }

    public void buscarApartirDoUsuarioLogado(){
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(ComandosSql.sqlBuscarNoBancoDeUsuarioLogado(), null);
        if(cursor.moveToFirst()){
            Cursor usuario = db.rawQuery(ComandosSql.sqlUsuarioApartirDoId(), new String[]{cursor.getString(0)});
            if (usuario.moveToFirst()){
                User userLogado = criarUsuario(usuario);
                Session.setUserAtual(userLogado);
            }
        }
    }
}
