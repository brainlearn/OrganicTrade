package mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.GUI;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import mpoo.bsi.ufrpe.organictrade.Infra.Persistencia.DatabaseHelper;
import mpoo.bsi.ufrpe.organictrade.Infra.Session;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Dominio.ItemListAdapter;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Dominio.TentItems;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Dominio.Tent;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Persistencia.TendaPersistencia;
import mpoo.bsi.ufrpe.organictrade.R;

public class PesquisarProdutosActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DatabaseHelper banco = Session.getDbAtual();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_produtos);
        Session.setContext(getBaseContext());

        final GridView listaDeItens = (GridView) findViewById(R.id.pesquisarProdutoGridItens);
        listaDeItens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TentItems item = (TentItems)listaDeItens.getAdapter().getItem(position);
                Toast.makeText(Session.getContext(),item.getNomeProduto(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        //-------------------------------------------------------------------------------//
        TendaPersistencia tendaPersistencia = new TendaPersistencia();
        Tent tent = tendaPersistencia.retornarTendaDosUsuarios();
        List<TentItems> tendaFinal = tent.getTent();
        //---------------------------------------------------------------------------------//
        ItemListAdapter adapter = new ItemListAdapter(tendaFinal);
        listaDeItens.setAdapter(adapter);
        //-------------------------------------------------------------------------------------//


    }
}
