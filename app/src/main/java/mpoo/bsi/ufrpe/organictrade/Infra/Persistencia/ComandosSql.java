package mpoo.bsi.ufrpe.organictrade.Infra.Persistencia;
public class ComandosSql {

    public static String sqlCreateUserTable(){
        String sqlCreateUserTable =
                "CREATE TABLE " +DatabaseHelper.getTableUserName()+ "( "
                        + DatabaseHelper.getColumnUserId()+" integer primary key autoincrement not null , "
                        + DatabaseHelper.getColumnUserName() + " text unique not null , "
                        + DatabaseHelper.getColumnUserPass() + " text not null , "
                        + DatabaseHelper.getColumnUserEmail() + " text unique not null , "
                        + DatabaseHelper.getColumnUserNome() + " text"
                        + ");";
        return(sqlCreateUserTable);
    }

    public static String sqlCreateItensdetendaTable(){
        String sqlCreateItensdetendaTable =
                "CREATE TABLE "+ DatabaseHelper.getTableItensdetendaName() + "( "
                        + DatabaseHelper.getColumnItensdetendaId()+" integer primary key autoincrement not null , "
                        + DatabaseHelper.getColumnItensdetendaQuantidade() +" text not null , "
                        + DatabaseHelper.getColumnItensdetendaValor() + " text not null , "
                        + DatabaseHelper.getColumnItensdetendaNomePoroduto()+ " text not null , "
                        + DatabaseHelper.getColumnItensdetendaUserId()+" integer , "
                        + "foreign key ( "+DatabaseHelper.getColumnItensdetendaUserId()+" ) references "+DatabaseHelper.getTableUserName()+" ( "+DatabaseHelper.getColumnUserId()+" )"
                        + ");";
        return(sqlCreateItensdetendaTable);

    }

    public static String sqlCreateUsuarioLogadoTable(){
        String sqlCreateUsuarioLogadoTable =
                "CREATE TABLE "+ DatabaseHelper.getTableUserLogadoName() + "( "
                        + DatabaseHelper.getColumnUserLogadoId()+" integer , "
                        + "foreign key ( "+DatabaseHelper.getColumnUserLogadoId()+" ) references "+DatabaseHelper.getTableUserName()+" ( "+DatabaseHelper.getColumnUserId()+" )"
                        + ");";
        return(sqlCreateUsuarioLogadoTable);
    }

    public static String sqlUsuarioQueTemOItem(){
        String sqlUsuarioQueTemOItem =
                "SELECT * FROM " + DatabaseHelper.getTableItensdetendaName() + " WHERE "
                        + DatabaseHelper.getColumnItensdetendaUserId() + " =?;";
        return (sqlUsuarioQueTemOItem);
    }

    public static String sqlUsuarioApartirDoLoginESenha(){
        String sqlUsuarioApartirDoLoginESenha =
                "SELECT * FROM "+DatabaseHelper.getTableUserName() +" WHERE "
                        +DatabaseHelper.getColumnUserName() +" =? AND "
                        +DatabaseHelper.getColumnUserPass() + " =?;";
        return (sqlUsuarioApartirDoLoginESenha);
    }

    public static String sqlUsuarioApartirDoId(){
        String sqlUsuarioApartirDoId=
                "SELECT * FROM "+DatabaseHelper.getTableUserName() +" WHERE "
                        +DatabaseHelper.getColumnUserId() +" =?;";
        return (sqlUsuarioApartirDoId);
    }

    public static String sqlLimparTabela(){
        String sqlLimparTabela = "DELETE FROM " + DatabaseHelper.getTableUserLogadoName();
        return (sqlLimparTabela);
    }

    public static String sqlBuscarNoBancoDeUsuarioLogado(){
        String sqlUsuarioApartirDoBancoUsuarioLogado =
                "SELECT "+ DatabaseHelper.getColumnUserLogadoId()+" FROM "+DatabaseHelper.getTableUserLogadoName() +";";
        return (sqlUsuarioApartirDoBancoUsuarioLogado);
    }

    public static String sqlUsuarioLogado(){
        String sqlUsuarioLogado =
                "SELECT * FROM " + DatabaseHelper.getTableUserName()+";";
        return (sqlUsuarioLogado);
    }

    public static String sqlDeslogarUsuario(){
        String sqlDeslogarUsuario =
                "SELECT * FROM "+DatabaseHelper.getTableUserLogadoName() +" WHERE "
                        +DatabaseHelper.getColumnUserLogadoId()+" =?;";
        return (sqlDeslogarUsuario);
    }

    public static String sqlDropTableUsuarioLogado(){
        String sqlDropTableUsuarioLogado = "DROP TABLE IF EXISTS " + DatabaseHelper.getTableUserLogadoName();
        return (sqlDropTableUsuarioLogado);
    }

    public static String sqlDropTableUsuario() {
        String sqlDropTableUsuario = "DROP TABLE IF EXISTS " + DatabaseHelper.getTableUserName();
        return(sqlDropTableUsuario);
    }

    public static String sqlDropTableItensDeTenda() {
        String sqlDropTableItensDeTenda = "DROP TABLE IF EXISTS " + DatabaseHelper.getTableItensdetendaName();
        return (sqlDropTableItensDeTenda);
    }
}
