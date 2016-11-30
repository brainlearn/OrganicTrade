package mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Item.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import mpoo.bsi.ufrpe.organictrade.Infra.Session;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.Item.dominio.Tent;
import mpoo.bsi.ufrpe.organictrade.R;


public class TentListAdapter extends ArrayAdapter<Tent> {
    private Context context;
    private ArrayList<Tent> tent = null;

    public TentListAdapter(ArrayList<Tent> tent) {
        super(Session.getContext(), 0, tent);
        this.tent = tent;
        this.context = Session.getContext();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Tent tenda = tent.get(position);

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.tent_listview_adapter, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tentImg);
        imageView.setImageBitmap(BitmapFactory.decodeFile(tenda.getImg()));

        TextView textoNomeTent = (TextView) view.findViewById(R.id.tentTxtNome);
        textoNomeTent.setText(tenda.getName());
        return view;
    }
}