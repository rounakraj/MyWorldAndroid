package com.app.shopchatmyworldra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.GPSTracker;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;

import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.BrandResources;
import com.app.shopchatmyworldra.pojo.CategoryResource;
import com.app.shopchatmyworldra.pojo.ModelResources;
import com.app.shopchatmyworldra.pojo.SubCategoryResources;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class PostsAdsActivity extends AppCompatActivity {

    RelativeLayout rlsubcategory, rlspin_brand, rlbrandbymodel;
    Spinner spin_category, spin_subcategory, spin_brand, spin_brandbymodel;
    Calendar c = Calendar.getInstance();
    String formattedDate;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView ivAdd;
    private EditText input_fullname;
    private EditText etDescription,etLocation;
    private TextInputLayout textInputLocation;
    private EditText etYear;
    private Spinner spSpinner;
    private EditText etContectno;
    private EditText etproductprice;
    private EditText etproductsplprice;
    private EditText etproductcondition;
    private Button btnpostadd;
    byte[] mData = new byte[0];
    byte[] mDatatwo = new byte[0];
    byte[] mDatathree = new byte[0];
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<String> imgsendarray = new ArrayList<String>();
    String productname, contactnum, year, loaction, description, price, splprice, invoice, procondition;
    String[] invoiceNames = {"Invoice Available", "Yes", "No"};
    ArrayList<String> categorylist = new ArrayList<String>();
    String categoryId;
    String subcategory;
    ArrayList<CategoryResource> categorytypeList = new ArrayList<CategoryResource>();

    ArrayList<String> subcategorylist = new ArrayList<String>();
    String subcategoryId;
    ArrayList<SubCategoryResources> subcategorytypeList = new ArrayList<SubCategoryResources>();


    ArrayList<String> brandlist = new ArrayList<String>();
    String brandId;
    ArrayList<BrandResources> brandtypeList = new ArrayList<BrandResources>();


    ArrayList<String> modellist = new ArrayList<String>();
    String modelId;
    ArrayList<ModelResources> modeltypeList = new ArrayList<ModelResources>();
    ArrayAdapter spin_subcategoryarray;
    public Snackbar snackbar;
    private String imgcount = "";
    int REQUEST_CAMERA = 0, SELECT_FILE = 3;
    private static Bitmap imagepic, imagepic1, imagepic2, imagepic3;
    byte[] imageArray1, imageArray2, imageArray3, imageArray;
    private LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    private GPSTracker gps;
    double latitude, longitude;
    private int PLACE_PICKER_REQUEST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_ads);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        ivAdd = (ImageView) findViewById(R.id.ivAdd);

        input_fullname = (EditText) findViewById(R.id.input_fullname);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etLocation = (EditText) findViewById(R.id.etLocation);
        textInputLocation = (TextInputLayout) findViewById(R.id.textInputLocation);
        etYear = (EditText) findViewById(R.id.etYear);
        spSpinner = (Spinner) findViewById(R.id.spSpinner);
        etContectno = (EditText) findViewById(R.id.etContectno);
        etproductprice = (EditText) findViewById(R.id.etproductprice);
        etproductsplprice = (EditText) findViewById(R.id.etproductsplprice);
        etproductcondition = (EditText) findViewById(R.id.etproductcondition);

        spin_subcategory = (Spinner) findViewById(R.id.spin_subcategory);
        spin_category = (Spinner) findViewById(R.id.spin_category);
        spin_brand = (Spinner) findViewById(R.id.spin_brand);
        spin_brandbymodel = (Spinner) findViewById(R.id.spin_brandbymodel);

        btnpostadd = (Button) findViewById(R.id.rl_submit);
        rlsubcategory = (RelativeLayout) findViewById(R.id.rlsubcategory);
        rlspin_brand = (RelativeLayout) findViewById(R.id.rlspin_brand);
        rlbrandbymodel = (RelativeLayout) findViewById(R.id.rlbrandbymodel);

        gps = new GPSTracker(PostsAdsActivity.this);
        getLatLong();

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcount = "0";
                selectImage();


            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcount = "1";
                selectImage();


            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcount = "2";
                selectImage();


            }
        });
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, invoiceNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpinner.setAdapter(aa);
        spSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    invoice = invoiceNames[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etYear.setText(sdf.format(myCalendar.getTime()));

            }

        };
        etYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PostsAdsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (CommanMethod.isOnline(PostsAdsActivity.this)) {

            ValidateGetCategory();
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(PostsAdsActivity.this, R.color.colorAccent));
            snackbar.show();
        }

        btnpostadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CommanMethod.isOnline(PostsAdsActivity.this)) {

                    productname = input_fullname.getText().toString().trim();
                    contactnum = etContectno.getText().toString().trim();
                    year = etYear.getText().toString().trim();
                    loaction = etLocation.getText().toString().trim();
                    description = etDescription.getText().toString().trim();
                    price = etproductprice.getText().toString().trim();
                    splprice = etproductsplprice.getText().toString().trim();
                    procondition = etproductcondition.getText().toString().trim();

                    if (validationpost()) {
                        imageArray1 = sendImage(imagepic1);
                        if (imagepic2 != null)
                            imageArray2 = sendImage(imagepic2);
                        if (imagepic3 != null)
                            imageArray3 = sendImage(imagepic3);
                        AddPosttsAsynTask addpost = new AddPosttsAsynTask();
                        addpost.execute();

                    }
                } else {
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(PostsAdsActivity.this, R.color.colorAccent));
                    snackbar.show();
                }


            }
        });

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryId = categorytypeList.get(position - 1).getCatId();
                    rlsubcategory.setVisibility(View.VISIBLE);
                    ValidateGetsubCategory();

                    Log.e("catId>>>>>>>>>>>>>", "" + categoryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    subcategoryId = subcategorytypeList.get(position - 1).getSubcatId();
                    rlspin_brand.setVisibility(View.VISIBLE);
                    ValidateGetBrand();
                    Log.e("subcatId>>>>>>>>>>>>>", "" + categoryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    brandId = brandtypeList.get(position - 1).getBrandId();
                    rlbrandbymodel.setVisibility(View.VISIBLE);
                    ValidateGetModel();
                    Log.e("brandId>>>>>>>>>>>>>", "" + brandId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_brandbymodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    modelId = modeltypeList.get(position - 1).getModelId();

                    Log.e("modelId>>>>>>>>>>>>>", "" + modelId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_subcategoryarray = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subcategorylist);
        spin_subcategoryarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_subcategory.setAdapter(spin_subcategoryarray);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        etLocation.setEnabled(false);

        textInputLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PLACE_PICKER_REQUEST = 4;
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    if (BOUNDS_MOUNTAIN_VIEW != null) {
                        intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    }

                    Intent intent = intentBuilder.build(PostsAdsActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PostsAdsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class AddPosttsAsynTask extends AsyncTask<String, Void, String> {
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            ProgressBarDialog.showProgressBar(PostsAdsActivity.this,"");



        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            ProgressBarDialog.dismissProgressDialog();

            Log.d("Response", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String responseMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {
                        CommanMethod.showAlert(responseMessage, PostsAdsActivity.this);
                        formattedDate = String.valueOf(System.currentTimeMillis());
                        MyPreferences.getActiveInstance(PostsAdsActivity.this).setlogintime(formattedDate);
                        MyApplication.cropped = null;
                        PostsAdsActivity.this.finish();

                    } else {
                        CommanMethod.showAlert(responseMessage, PostsAdsActivity.this);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error), PostsAdsActivity.this);

            }
        }

        private String callService() {
            String url = WebServices.adduserProduct;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {


                client.connectForMultipart();
                client.addFormPart("postedBy", MyPreferences.getActiveInstance(PostsAdsActivity.this).getUserId());
                client.addFormPart("productName", productname);
                client.addFormPart("contactNum", contactnum);
                client.addFormPart("productPrice", price);
                client.addFormPart("productsplPrice", splprice);
                client.addFormPart("productDesp", description);
                client.addFormPart("productCondition", procondition);
                client.addFormPart("catId", categoryId);
                client.addFormPart("subcatId", subcategoryId);
                client.addFormPart("invoice", invoice);
                client.addFormPart("brandId", brandId);
                client.addFormPart("brandmodelId", modelId);
                client.addFormPart("location", loaction);
                client.addFormPart("productYear", year);
                Log.e("imageArray1", "" + imageArray1);
                Log.e("imageArray2", "" + imageArray2);
                Log.e("imageArray3", "" + imageArray3);
                if (imageArray1 != null) {
                    client.addFilePart("productImage1", ".png", imageArray1);
                }
                if (imageArray2 != null) {
                    client.addFilePart("productImage2", ".png", imageArray2);
                }
                if (imageArray3 != null) {
                    client.addFilePart("productImage3", ".png", imageArray3);
                }
                Log.d("client", client.toString());
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }

    //Asyntask_getcat

    protected void ValidateGetCategory() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        client.post(WebServices.GETCATEGORY, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PostsAdsActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultCatlist(response.toString(), PostsAdsActivity.this);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultCatlist(String response, Context LognActivity) {

        JSONObject object;

        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");

                Log.d("object", "" + object);
                if (success.equals("200")) {

                    Log.d("success", "" + success);
                    try {
                        JSONArray jsonarry = object.getJSONArray("categorylist");
                        categorylist.clear();
                        categorytypeList.clear();
                        categorylist.add("Select Category Type");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            CategoryResource details = new CategoryResource();

                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setCatId(jsonObject2.getString("catId"));
                            details.setCategoryname(jsonObject2.getString("catName"));

                            categorytypeList.add(details);
                            categorylist.add(jsonObject2.getString("catName"));
                        }

                        Log.d("categorylist", "" + categorylist.size());

                        ArrayAdapter categoryarray = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorylist);
                        categoryarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_category.setAdapter(categoryarray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

// getsubcategory

    protected void ValidateGetsubCategory() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("catId", categoryId);
        client.post(WebServices.GETSUBCATEGORY, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PostsAdsActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultsubCatlist(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultsubCatlist(String response) {

        JSONObject object;

        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");
                subcategorylist.clear();
                subcategorytypeList.clear();
                Log.d("object", "" + object);
                if (success.equals("200")) {
                    Log.d("success", "" + success);
                    try {
                        JSONArray jsonarry = object.getJSONArray("subcategory");
                        subcategorylist.add("Select Subcategory Type");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            SubCategoryResources details = new SubCategoryResources();

                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setSubcatId(jsonObject2.getString("subcatId"));
                            details.setSubcatName(jsonObject2.getString("subcatName"));
                            //subcategory=jsonObject2.getString("subcatId");

                            subcategorytypeList.add(details);
                            subcategorylist.add(jsonObject2.getString("subcatName"));
                        }

                        Log.d("subcategorylist", "" + categorylist.size());

                        spin_subcategoryarray.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    subcategorylist.add(message);
                    spin_subcategoryarray.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    // Get Brand AsynTask

    protected void ValidateGetBrand() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("subcatId", subcategoryId);
        client.post(WebServices.GetBrand, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PostsAdsActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultbrand(response.toString(), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultbrand(String response, Context LognActivity) {

        JSONObject object;

        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");

                Log.d("object", "" + object);
                if (success.equals("200")) {

                    Log.d("success", "" + success);
                    try {
                        JSONArray jsonarry = object.getJSONArray("brand");
                        brandlist.clear();
                        brandtypeList.clear();
                        brandlist.add("Select Brand Type");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            BrandResources details = new BrandResources();

                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setBrandId(jsonObject2.getString("brandId"));
                            details.setBrandName(jsonObject2.getString("brandName"));

                            brandtypeList.add(details);
                            brandlist.add(jsonObject2.getString("brandName"));
                        }

                        Log.d("brandlist", "" + brandlist.size());

                        ArrayAdapter spin_brandlist = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brandlist);
                        spin_brandlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_brand.setAdapter(spin_brandlist);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


// GEt MODEL ASYNTASK


    protected void ValidateGetModel() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("brandId", brandId);
        client.post(WebServices.GetBrandbyModel, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PostsAdsActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultmodel(response.toString(), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PostsAdsActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultmodel(String response, Context LognActivity) {

        JSONObject object;

        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");

                Log.d("object", "" + object);
                if (success.equals("200")) {

                    Log.d("success", "" + success);
                    try {
                        JSONArray jsonarry = object.getJSONArray("model");
                        modellist.clear();
                        modeltypeList.clear();
                        modellist.add("Select Model Type");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            ModelResources details = new ModelResources();

                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setModelId(jsonObject2.getString("brandmodelId"));
                            details.setMdelName(jsonObject2.getString("brandmodelName"));

                            modeltypeList.add(details);
                            modellist.add(jsonObject2.getString("brandmodelName"));
                        }

                        Log.d("modellist", "" + modellist.size());

                        ArrayAdapter spin_modellist = new ArrayAdapter(this, android.R.layout.simple_spinner_item, modellist);
                        spin_modellist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_brandbymodel.setAdapter(spin_modellist);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if request code is same we pass as argument in startActivityForResult
        if (requestCode == 1) {
            //create instance of File with same name we created before to get image from storage
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            //Crop the captured image using an other intent
            try {
            /*the user's device may not support cropping*/
                cropCapturedImage(Uri.fromFile(file));
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(PostsAdsActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (requestCode == 3) {
            //create instance of File with same name we created before to get image from storage
            Uri selectedImageUri = null;
            if (data != null)                  // Todo Without using .... select galary and then back as well as  show gallery icon click in layout
                selectedImageUri = data.getData();
            //Crop the captured image using an other intent
            try {
            /*the user's device may not support cropping*/
                cropCapturedImage(selectedImageUri);
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(PostsAdsActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        if (requestCode == 2) {
            //Create an instance of bundle and get the returned data
            Log.d("click ", "after capture image");
            Log.e("click ", "after capture image");
            Bundle extras = null;
            Log.d("click ", "extras capture image" + extras);
            if (data != null)
                extras = data.getExtras();
            //get the cropped bitmap from extras
            Log.e("click ", "extras capture image" + extras);
            imagepic = null;
            if (extras != null) {
                imagepic = extras.getParcelable("data");
            }
            //set image bitmap to image view
            if (imagepic != null) {
                if (imgcount.equals("0")) {
                    image1.setImageBitmap(imagepic);
                    imagepic1 = imagepic;
                }
                if (imgcount.equals("1")) {

                    image2.setImageBitmap(imagepic);
                    imagepic2 = imagepic;

                }
                if (imgcount.equals("2")) {

                    image3.setImageBitmap(imagepic);
                    imagepic3 = imagepic;
                }
            }

        }
        if (requestCode ==4&& resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(PostsAdsActivity.this, data);
            final CharSequence address = place.getAddress();
            LatLng latlong = place.getLatLng();
            latitude = latlong.latitude;
            longitude = latlong.longitude;
            etLocation.setText(address);
        }
    }

    public byte[] sendImage(Bitmap bm) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        imageArray = bos.toByteArray();
        return imageArray;
    }


    private boolean validationpost() {


        if (imagepic1 == null) {
            CommanMethod.showAlert("Please add  image.", PostsAdsActivity.this);
            return false;
        } else if (spin_category.getSelectedItem() != null && spin_category.getSelectedItem().equals("Select Category Type")) {
            CommanMethod.showAlert("Please select category.", PostsAdsActivity.this);
            return false;
        } else if (spin_subcategory.getSelectedItem() != null && spin_subcategory.getSelectedItem().equals("Select Subcategory Type")) {
            CommanMethod.showAlert("Please select subcategory.", PostsAdsActivity.this);
            return false;
        } else if (spin_brand.getSelectedItem() != null && spin_brand.getSelectedItem().equals("Select Brand Type")) {
            CommanMethod.showAlert("Please select brand.", PostsAdsActivity.this);
            return false;
        } else if (spin_brandbymodel.getSelectedItem() != null && spin_brandbymodel.getSelectedItem().equals("Select Model Type")) {
            CommanMethod.showAlert("Please select brand by model.", PostsAdsActivity.this);
            return false;
        } else if (input_fullname.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter product name.", PostsAdsActivity.this);
            return false;
        } else if (etContectno.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter contact number.", PostsAdsActivity.this);
            return false;
        } else if (spSpinner.getSelectedItem().equals("Invoice")) {
            CommanMethod.showAlert("Please select invoice.", PostsAdsActivity.this);
            return false;
        } else if (etYear.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter select year.", PostsAdsActivity.this);
            return false;
        } else if (etLocation.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter location.", PostsAdsActivity.this);
            return false;
        } else if (etproductprice.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter product price.", PostsAdsActivity.this);
            return false;
        } else if (etproductsplprice.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter special product price.", PostsAdsActivity.this);
            return false;
        } else if (etproductcondition.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter product condition.", PostsAdsActivity.this);
            return false;
        } else if (etDescription.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter product description.", PostsAdsActivity.this);
            return false;
        }
        return true;

    }


    //create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    //TODO call

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PostsAdsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    //TODO imVCature_pic=(ImageView)findViewById(R.id.imVCature_pic);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            /*create instance of File with name img.jpg*/
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            /*put uri as extra in intent object*/
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            /*start activity for result pass intent as argument and request code */
                    startActivityForResult(intent, 1);
                } else if (items[item].equals("Choose from Library")) {
//TODO                    imVCature_pic=(ImageView)findViewById(R.id.imVCature_pic);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    //  File file = new File(Intent.createChooser(intent, "Select File") + File.separator + "img.jpg");

                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latitude....=", ">>>>>>>>>>>" + latitude);
            Log.d("longitude....=", ">>>>>>>>>>" + longitude);
            BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(latitude, longitude), new LatLng(latitude, longitude));

        } else {
            gps.showSettingsAlert();
        }
    }




}
