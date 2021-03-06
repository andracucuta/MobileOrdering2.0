package com.example.cucutaae.mobileordering10;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucutaae.mobileordering10.aboutus.AddAboutUsActivity;
import com.example.cucutaae.mobileordering10.location.AddPlaceLocationActivity;
import com.example.cucutaae.mobileordering10.menu.AddCategoryActivity;
import com.example.cucutaae.mobileordering10.menu.AddProductActivity;
import com.example.cucutaae.mobileordering10.menu.MenuProduct;
import com.example.cucutaae.mobileordering10.menu.MenuProductAdapter;
import com.example.cucutaae.mobileordering10.menu.ProductItemViewActivity;
import com.example.cucutaae.mobileordering10.menu.ProductListActivity;
import com.example.cucutaae.mobileordering10.order.OrderListAdapter;
import com.example.cucutaae.mobileordering10.signin.SignInClientActivity;
import com.example.cucutaae.mobileordering10.signin.SignInWaiterActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainWaiterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TableAdapter adapter;

    private List<Table> mTableList = new ArrayList<>();
    private ListView lvTableList;

    private  FirebaseAuth firebaseAuth;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refTable = database.getReference("Table");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main_waiter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView name = (TextView) header.findViewById(R.id.tvWaiterName);
        TextView email = (TextView)header.findViewById(R.id.tvWaiterEmail);
        name.setText(user.getEmail().split("@")[0]);
        email.setText(user.getEmail());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        lvTableList = (ListView) findViewById(R.id.lvTableList);

        refTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Table table = snapshot.getValue(Table.class);
                    mTableList.add(table);
                }

                adapter = new TableAdapter(MainWaiterActivity.this,  mTableList);
                lvTableList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();

            finish();

            Intent intentSignOut = new Intent(MainWaiterActivity.this, SignInWaiterActivity.class);
            MainWaiterActivity.this.startActivity(intentSignOut);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addProduct) {
            startActivity(new Intent(this,AddProductActivity.class));
        } else if (id == R.id.nav_category) {
            startActivity(new Intent(this,AddCategoryActivity.class));
        } else if (id == R.id.nav_placeLocation) {
            startActivity(new Intent(this,AddPlaceLocationActivity.class));
        } else if (id == R.id.nav_placeAboutUs) {
            startActivity(new Intent(this,AddAboutUsActivity.class));
        }else if (id == R.id.nav_addTable) {
            startActivity(new Intent(this,AddTableActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
