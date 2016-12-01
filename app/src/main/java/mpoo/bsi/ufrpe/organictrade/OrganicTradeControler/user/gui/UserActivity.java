package mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.user.gui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import mpoo.bsi.ufrpe.organictrade.Infra.Session;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.dominio.Tent;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.gui.RegisterTentActivity;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.gui.TentActivity;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.gui.TentListAdapter;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.persistencia.TentItemsPersistence;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.user.dominio.User;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.gui.SearchProductsActivity;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.item.persistencia.TentPersistence;
import mpoo.bsi.ufrpe.organictrade.OrganicTradeControler.user.persistencia.UserPersistence;
import mpoo.bsi.ufrpe.organictrade.R;

public class UserActivity extends AppCompatActivity {
    private long lastBackPressTime = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    private Toast toast;
    private ArrayList<Tent> finalTent;
    private ListView listOfTents;
    private TentListAdapter adapter;
    private TentPersistence tentPersistence = new TentPersistence();
    private UserPersistence crud = new UserPersistence();
    private String imageUser;
    private ImageView imageView;
    private ImageView addBtn;
    private ImageView editBtn;
    private ImageView searchBtn;
    private ImageView logoutBtn;
    private Tent tentSelected;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_tent, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.deletar:
                delete(info);
                return true;
            case R.id.expandir:
                toExpand(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this,getText(R.string.tstPressAgainToCloseApp), Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            crud.setImageUser(picturePath);
            Session.getCurrentUser().setImage(picturePath);
            changeImgUser();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Session.setContext(getBaseContext());
        imageUser = Session.getCurrentUser().getImage();
        setNameUser();
        loadImgUser();
        pupulateListView();
        loadAddBtn();
        loadEditBtn();
        loadSearchBtn();
        loadLogoutBtn();
        setFunctionImgUser();
    }

    private void pupulateListView() {
        listOfTents = (ListView) findViewById(R.id.usuarioListViewList);
        TentPersistence tentPersistence = new TentPersistence();
        finalTent = tentPersistence.getTentOfUser(Session.getCurrentUser().getId_user());
        adapter = new TentListAdapter(finalTent);
        listOfTents.setAdapter(adapter);
        registerForContextMenu(listOfTents);
    }

    private void changeImgUser() {
        imageUser = Session.getCurrentUser().getImage();
        imageView = (ImageView) findViewById(R.id.profilePicture);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imageUser));
    }

    private void delete(AdapterView.AdapterContextMenuInfo info ) {
        TentItemsPersistence tentItemsPersistence = new TentItemsPersistence();
        tentSelected =(Tent) listOfTents.getAdapter().getItem(info.position);
        tentPersistence.deleteTent(tentSelected.getTentId());
        finalTent.remove(info.position);
        tentItemsPersistence.deleteAllItemsOfTent(tentSelected.getTentId());
        adapter.notifyDataSetChanged();
    }

    private void toExpand(AdapterView.AdapterContextMenuInfo info) {
        tentSelected =(Tent) listOfTents.getAdapter().getItem(info.position);
        Session.setTentSelected(tentSelected);
        Intent i = new Intent(Session.getContext(),TentActivity.class);
        startActivity(i);
    }

    private void setNameUser() {
        TextView text = (TextView)findViewById(R.id.userTextName);
        String[] nome = Session.getCurrentUser().getName().split(" ");
        text.setText(nome[0]);
    }

    private void loadImgUser() {
        imageView = (ImageView) findViewById(R.id.profilePicture);
        if (!(imageUser == null)){
            imageView.setImageBitmap(BitmapFactory.decodeFile(imageUser));
        }else {
            imageView.setImageResource(R.drawable.no_img_icon);
        }
    }

    private void setFunctionImgUser() {
        final ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void loadLogoutBtn() {
        initializeLogoutBtn();
        setFunctionLogoutBtn();
    }

    private void setFunctionLogoutBtn() {
        logoutBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                logoutBtn.setImageResource(R.mipmap.ic_logoutonclick);
                return false;
            }
        });
        logoutBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayToastAboveButton(v,R.string.txtLogout);
                return false;
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crud.userLogoff();
                Session.setCurrentUser(new User());
                Intent p = new Intent(Session.getContext(),LoginActivity.class);
                logoutBtn.setImageResource(R.mipmap.ic_logout);
                startActivity(p);
                finish();
            }
        });
    }

    private void initializeLogoutBtn() {
        logoutBtn =(ImageView) findViewById(R.id.userImgBtnLogout);
        logoutBtn.setImageResource(R.mipmap.ic_logout);
    }

    private void loadSearchBtn() {
        initializeSearchBtn();
        setFunctionSearchBtn();
    }

    private void setFunctionSearchBtn() {
        searchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchBtn.setImageResource(R.mipmap.ic_searchonclick);
                return false;
            }
        });
        searchBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayToastAboveButton(v,R.string.txtSearch);
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Session.getContext(),SearchProductsActivity.class);
                searchBtn.setImageResource(R.mipmap.ic_search);
                startActivity(p);

            }
        });
    }

    private void initializeSearchBtn() {
        searchBtn =(ImageView) findViewById(R.id.userImgBtnToSearch);
        searchBtn.setImageResource(R.mipmap.ic_search);
    }

    private void loadEditBtn() {
        initializeEditBtn();
        setFunctionEditBtn();
    }

    private void setFunctionEditBtn() {
        editBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editBtn.setImageResource(R.mipmap.ic_editonclick);
                return false;
            }
        });
        editBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayToastAboveButton(v,R.string.txtEditPerfil);
                return false;
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Session.getContext(),EditRegisterUserActivity.class);
                editBtn.setImageResource(R.mipmap.ic_edit);
                startActivity(p);

            }
        });
    }

    private void initializeEditBtn() {
        editBtn = (ImageView)findViewById(R.id.userImgBtnToEdit);
        editBtn.setImageResource(R.mipmap.ic_edit);
    }

    private void loadAddBtn() {
        initializeAddBtn();
        setFunctionAddBtn();
    }

    private void setFunctionAddBtn() {
        addBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayToastAboveButton(v,R.string.txtNewItem);
                return false;
            }
        });
        addBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addBtn.setImageResource(R.mipmap.ic_addonclick);
                return false;
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Session.getContext(),RegisterTentActivity.class);
                addBtn.setImageResource(R.mipmap.ic_add);
                startActivity(p);
            }
        });
    }

    private void initializeAddBtn() {
        addBtn = (ImageView) findViewById(R.id.userImgBtnToCadastro);
        addBtn.setImageResource(R.mipmap.ic_add);
    }

    private void displayToastAboveButton(View v, int messageId) {
        int xOffset = 0;
        int yOffset = 0;
        Rect gvr = new Rect();

        View parent = (View) v.getParent();
        int parentHeight = parent.getHeight();

        if (v.getGlobalVisibleRect(gvr))
        {
            View root = v.getRootView();

            int halfWidth = root.getRight() / 2;
            int halfHeight = root.getBottom() / 2;

            int parentCenterX = ((gvr.right - gvr.left) / 2) + gvr.left;

            int parentCenterY = ((gvr.bottom - gvr.top) / 2) + gvr.top;

            if (parentCenterY <= halfHeight)
            {
                yOffset = -(halfHeight - parentCenterY) - parentHeight;
            }
            else
            {
                yOffset = (parentCenterY - halfHeight) - parentHeight;
            }

            if (parentCenterX < halfWidth)
            {
                xOffset = -(halfWidth - parentCenterX);
            }

            if (parentCenterX >= halfWidth)
            {
                xOffset = parentCenterX - halfWidth;
            }
        }

        Toast toast = Toast.makeText(Session.getContext(), messageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, xOffset, yOffset);
        toast.show();
    }
}