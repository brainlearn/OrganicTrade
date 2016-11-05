package mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Dominio;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import mpoo.bsi.ufrpe.organictrade.Infra.Persistencia.ComandosSql;
import mpoo.bsi.ufrpe.organictrade.Infra.Persistencia.DatabaseHelper;
import mpoo.bsi.ufrpe.organictrade.Infra.Session;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Persistencia.ProductPersistence;
import mpoo.bsi.ufrpe.organictrade.R;

public class ItemListAdapter extends ArrayAdapter<TentItems> {
    private SQLiteDatabase db;
    private DatabaseHelper banco = Session.getDbAtual();
    private Context context;
    private List<TentItems> tentItems = null;
    private ProductPersistence productPersistence = new ProductPersistence();

    public ItemListAdapter(List<TentItems> tentItems) {
        super(Session.getContext(),0, tentItems);
        this.tentItems = tentItems;
        this.context = Session.getContext();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TentItems itenDeTenda = tentItems.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_adapter, null);

        TextView textoNomeProduto = (TextView) view.findViewById(R.id.itemTxtNome);
        textoNomeProduto.setText(productPersistence.nameProductById(itenDeTenda.getProdutoId()));

        TextView textoPriceProduto = (TextView)view.findViewById(R.id.itemTxtPrice);
        textoPriceProduto.setText(itenDeTenda.getValor());

        return view;
    }
}