package com.app.shopchatmyworldra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.GPSTracker;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class ProductDetailActivity extends AppCompatActivity {
    LinearLayout llchataddcart,llLocation;
    Toolbar toolbar;
    String time;
    String productId,placecallEmailId;
    String userName,chtuserName;
    private TextView tvAddcart,txtmodel,txtyear,txtdescprition,tvchat,tvAddress,awaylocation,addtime,tvParcentage,tvReportContent;
    TextView txtproductname,txtprice,tvUploadName,tvViewProfile,txtbrand;
    public static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> mproductImgs = new ArrayList<String>();
    String[] bankNames={"Quantity","1","2","3","4","5","6","7","8","9","10"};
    public ImageView ivFavroiet,facebook,watsup,twiter,ivdFavroiet,ivMap;
    private Spinner spQuanty;
    private String quantity;
    private String userId;
    private String mProductImage;
    private StringBuilder stringBuilder;
    private ScrollView scorll;
    private String latiTude;
    private String longiTude;
    long now = 0;
    private GPSTracker gps;
    double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getReferanceId();

        Intent intent = getIntent();
        productId  = intent.getStringExtra("productId");
        userName  = intent.getStringExtra("userName");
        Log.e("producctid",productId);
        GETnumberofhits();


        validateProductDetails();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ivFavroiet = (ImageView) findViewById(R.id.ivFavroiet);
        ivdFavroiet = (ImageView) findViewById(R.id.ivdFavroiet);
        mPager = (ViewPager)findViewById(R.id.pager);
        addtime=(TextView)findViewById(R.id.addtime);
        scorll=(ScrollView)findViewById(R.id.scorll);
        tvParcentage=(TextView)findViewById(R.id.tvParcentage);
        tvReportContent=(TextView)findViewById(R.id.tvReportContent);
        ivMap=(ImageView) findViewById(R.id.ivMap);
        llLocation=(LinearLayout) findViewById(R.id.llLocation);
        if(userName.equalsIgnoreCase("Myworld")){
            llLocation.setVisibility(View.GONE);
        }else {
            llLocation.setVisibility(View.VISIBLE);
        }

        now = System.currentTimeMillis();
        ivFavroiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivdFavroiet.setVisibility(View.VISIBLE);
                ivFavroiet.setVisibility(View.GONE);
                DISLIKEWISHLIST(productId,userName);

            }
        });
        ivdFavroiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivdFavroiet.setVisibility(View.GONE);
                ivFavroiet.setVisibility(View.VISIBLE);
                ADDWISHLIST(productId,userName);


            }
        });

            tvAddcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tvAddcart.getText().toString().equalsIgnoreCase("Add to Cart")) {
                        if (quantity != null) {
                            validateAddCart();
                        } else {
                            CommanMethod.showAlert("Please select Quantity", ProductDetailActivity.this);
                        }
                    }else {
                        if(CommanMethod.isNetworkAvailable(ProductDetailActivity.this)){
                            validateallBlock(userId,"");
                        }else {
                            CommanMethod.showSnack(v,"Please check your internate connection");
                        }
                    }
                }
            });

            tvReportContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialogMessage(userId,ProductDetailActivity.this);
                }
            });


        tvchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(userName!=null&&userName.equals("Myworld"))
                {
                    Intent in =new Intent(ProductDetailActivity.this,MessageActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }else {
                    Intent in =new Intent(ProductDetailActivity.this,UserChatMessageActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("Recipient",placecallEmailId);
                    in.putExtra("userId",userId);
                    in.putExtra("userName",chtuserName);
                    startActivity(in);

                }
            }
        });

        if(userName!=null&&userName.equals("Myworld"))
        {
            tvAddcart.setText("Add to Cart");
            tvViewProfile.setVisibility(View.GONE);
            tvReportContent.setVisibility(View.GONE);
            tvAddcart.setText("Add to Cart");
        }else {
            tvAddcart.setText("Make an Offer");
            tvViewProfile.setVisibility(View.VISIBLE);
            tvReportContent.setVisibility(View.VISIBLE);
            tvAddcart.setText("Block");

        }

        tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(ProductDetailActivity.this,UserProfileActivity.class);
                intent.putExtra("UserId",userId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if(tvAddcart.getText().toString().equals("Make an Offer"))
        {
            tvAddcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in =new Intent(ProductDetailActivity.this,UserChatMessageActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("Recipient",placecallEmailId);
                    in.putExtra("userId",userId);
                    in.putExtra("userName",chtuserName);
                    startActivity(in);
                }
            });
        }
        ArrayAdapter spin_subcategoryarray = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bankNames);
        spin_subcategoryarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuanty.setAdapter(spin_subcategoryarray);

        spQuanty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    quantity=parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile(ProductDetailActivity.this);
            }
        });
        watsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile(ProductDetailActivity.this);
            }
        });
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile(ProductDetailActivity.this);

            }
        });

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        gps = new GPSTracker(ProductDetailActivity.this);
        getLatLong();


        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f",Double.parseDouble(latiTude), Double.parseDouble(longiTude));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);

                    /*String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", latitude, latitude, "Home Sweet Home", Double.parseDouble(latiTude), Double.parseDouble(longiTude), "Where the party is at");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);*/
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });


    }
    public void getReferanceId()
    {
        txtproductname=(TextView)findViewById(R.id.txtproductname);
        tvAddcart=(TextView)findViewById(R.id.tvAddcart);
        txtprice=(TextView)findViewById(R.id.txtprice);
        tvViewProfile=(TextView)findViewById(R.id.tvViewProfile);
        tvUploadName=(TextView)findViewById(R.id.tvUploadName);
        txtbrand=(TextView)findViewById(R.id.txtbrand);
        txtmodel=(TextView)findViewById(R.id.txtmodel);
        tvAddress=(TextView)findViewById(R.id.tvAddress);
        tvchat=(TextView)findViewById(R.id.tvchat);
        txtyear=(TextView)findViewById(R.id.txtyear);
        awaylocation=(TextView)findViewById(R.id.awaylocation);
        txtdescprition=(TextView)findViewById(R.id.txtdescprition);
        spQuanty=(Spinner)findViewById(R.id.spQuanty);
        llchataddcart=(LinearLayout)findViewById(R.id.llchataddcart);
        facebook=(ImageView)findViewById(R.id.facebook);
        watsup=(ImageView)findViewById(R.id.watsup);
        twiter=(ImageView)findViewById(R.id.twiter);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void validateProductDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("productId",productId);
        params.add("userName",userName);
        client.post(WebServices.GetproductDetails, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("ResponseProductDtails", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                if (response.get("productlist") instanceof JSONArray) {

                    scorll.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = null;
                    jsonArray = response.getJSONArray("productlist");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonArray.get(i);

                        placecallEmailId=obj.getString("emailId");

                        if(MyPreferences.getActiveInstance(ProductDetailActivity.this).getEmailId().equals(placecallEmailId)){
                            llchataddcart.setVisibility(View.INVISIBLE);
                        }else {
                            llchataddcart.setVisibility(View.VISIBLE);
                        }

                        if (obj.getString("productName").toString().trim().equals("null") || obj.getString("productName").equals("")) {
                            txtproductname.setText("");
                        } else {
                            txtproductname.setText(obj.getString("productName").toString().trim());
                        }if (obj.optString("latiTude").toString().trim().equals("null") || obj.optString("latiTude").equals("")) {

                        } else {
                            latiTude=obj.optString("latiTude");

                        }if (obj.optString("longiTude").toString().trim().equals("null") || obj.optString("longiTude").equals("")) {

                        } else {
                            longiTude=obj.optString("longiTude");

                        }if (obj.getString("productPrice").toString().trim().equals("null") || obj.getString("productPrice").equals("")) {
                            txtprice.setText("");
                        } else {
                            txtprice.setText("INR "+obj.getString("productPrice").toString().trim());
                        }if (obj.getString("userName").toString().trim().equals("null") || obj.getString("userName").equals("")) {
                            tvUploadName.setText("");
                        } else {
                            tvUploadName.setText(obj.getString("userName").toString().trim());
                            chtuserName=obj.getString("userName").toString().trim();
                        }if (obj.getString("brandName").toString().trim().equals("null") || obj.getString("brandName").equals("")) {
                            txtbrand.setText("");
                        } else {
                            txtbrand.setText(obj.getString("brandName").toString().trim());
                        }
                        if (obj.getString("Percent").toString().trim().equals("null") || obj.getString("Percent").equals("")) {
                            tvParcentage.setText("");
                        } else {
                            tvParcentage.setText(obj.getString("Percent")+"% off");
                        }
                        if (obj.getString("brandmodelName").toString().trim().equals("null") || obj.getString("brandmodelName").equals("")) {
                            txtmodel.setText("");
                        } else {
                            txtmodel.setText(obj.getString("brandmodelName").toString().trim());
                        }if (obj.getString("productCreated").toString().trim().equals("null") || obj.getString("productCreated").equals("")) {
                            txtyear.setText("");
                        } else {

                            String creationdate = obj.getString("productCreated");
                            txtyear.setText(creationdate.split(" ")[0]);
                            long datePrev = 0;
                            long now = 0;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                if (!obj.getString("productCreated").equals("null")&&!obj.getString("productCreated").equals("")) {
                                    Date date = sdf.parse(obj.getString("productCreated"));
                                    datePrev = date.getTime();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            now = System.currentTimeMillis();
                            addtime.setText("AD Posted-"+" "+ CommanMethod.getDiference(now -datePrev)+" "+"ago");
                        }if (obj.getString("productDesp").toString().trim().equals("null") || obj.getString("productDesp").equals("")) {
                            txtdescprition.setText("");
                        } else {
                            txtdescprition.setText(obj.getString("productDesp").toString().trim());
                        }if (obj.optString("location").toString().trim().equals("null") || obj.optString("location").equals("")) {
                            tvAddress.setText("");
                            awaylocation.setText("");
                        } else {
                            tvAddress.setText(obj.getString("location").toString().trim());
                            awaylocation.setText(obj.getString("location").toString().trim());
                        }
                        if (obj.optString("userId").toString().trim().equals("null") || obj.optString("userId").equals("")) {

                        } else {
                            userId=obj.optString("userId");
                        }

                            mproductImgs.add(obj.getString("productImage1"));
                            mProductImage=obj.getString("productImage1");
                            mproductImgs.add(obj.getString("productImage2"));
                            mproductImgs.add(obj.getString("productImage3"));
                    }
                    init();


                }else{

                    CommanMethod.showAlert("No Product Details",ProductDetailActivity.this);
                    scorll.setVisibility(View.GONE);

                }
            }else {

                CommanMethod.showAlert(message,ProductDetailActivity.this);
                scorll.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void validateAddCart() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("productId",productId);
        params.add("userId", MyPreferences.getActiveInstance(ProductDetailActivity.this).getUserId());
        params.add("quantity",quantity);
        client.post(WebServices.addtoCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {

                                Intent in=new Intent(ProductDetailActivity.this,CartlistActivity.class);
                                startActivity(in);
                                Toast.makeText(ProductDetailActivity.this,message,Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                CommanMethod.showAlert(message,ProductDetailActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


    private void init() {


       // System.out.println("check size of mBannerImgs :: "+mBannerImgs);
        mPager.setAdapter(new SlidingImage_Adapter(ProductDetailActivity.this, mproductImgs));

        //System.out.println("check size of mBannerImgs below  :: "+mBannerImgs);
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);


        NUM_PAGES = mproductImgs.size();


        // Auto start of viewpager
       final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
    private void ADDWISHLIST(String productId,String Name) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ProductDetailActivity.this).getUserId());
        params.add("productId", productId);
        params.add("userName", Name);
        client.post(WebServices.ADDWISHLIST, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult1(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }


    private void parseResult1(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                CommanMethod.showAlert(message,ProductDetailActivity.this);

            }else {

                CommanMethod.showAlert(message,ProductDetailActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void GETnumberofhits() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("productId",productId);
        params.add("userName",userName);
        client.post(WebServices.numberofhits, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultnumber(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }

    private void parseResultnumber(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                Log.e("numberofhitsResponse", "--->>" + response.toString(2));

            }else {
                CommanMethod.showAlert(message,ProductDetailActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Method to share any image.
    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
        String imagePath = Environment.getExternalStorageDirectory()
                + "/myImage.png";

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }


    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    private void DISLIKEWISHLIST(String productId, String Name) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ProductDetailActivity.this).getUserId());
        params.add("productId", productId);
        params.add("userName", Name);
        client.post(WebServices.DISLIKEWISHLIST, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult1(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });
    }


    private void parseResult1(String result,int Position) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                Toast.makeText(ProductDetailActivity.this,message,Toast.LENGTH_SHORT).show();
            }else {

                CommanMethod.showAlert(message,ProductDetailActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void shareFile(Activity activity) {

        stringBuilder = new StringBuilder();
        stringBuilder.append("Hey, \n\n"+"Check out My World app. They Share Product, accessories and more for men & women, Download the app!\n "+mProductImage);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"My World");
        sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);

    }

    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("latitude....=", ">>>>>>>>>>>" + latitude);
            Log.d("longitude....=", ">>>>>>>>>>" + longitude);
        } else {
            gps.showSettingsAlert();
        }
    }

    public void AlertDialogMessage(final String userId1,final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.message_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.show();
        final EditText messageBox = (EditText) dialogLayout.findViewById(R.id.edit_chat_message);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);
        final TextView tvCancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);
        final TextView tvBlock = (TextView) dialogLayout.findViewById(R.id.tvBlock);
        tvBlock.setVisibility(View.GONE);
        messageBox.setVisibility(View.VISIBLE);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBox.getText().toString().trim().length()>0)
                {
                    String message = messageBox.getText().toString().trim();
                    validateallBlock(userId1,message);

                    customAlertDialog.dismiss();
                }else {
                    CommanMethod.showSnack(v,"Please write some messages");
                }


            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();

            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setVisibility(View.GONE);
                validateallBlock(userId1,"");

            }
        });
    }

    protected void validateallBlock(String blockUserId,String report_text) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("login_user_id", MyPreferences.getActiveInstance(ProductDetailActivity.this).getUserId());
        params.add("block_user_id", blockUserId);
        if(report_text!=""){

            params.add("report", "1");
            params.add("report_text", report_text);
        }
        client.post(WebServices.blockAndReport, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ProductDetailActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        ProgressBarDialog.dismissProgressDialog();

                        parseResultBlockReport(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ProductDetailActivity.this);
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

    private void parseResultBlockReport(String result) {

        try {
            JSONObject response = new JSONObject(result);
            if (response.getString("responseCode").equals("200"))
            {
                Toast.makeText(ProductDetailActivity.this,response.getString("responseMessage"),Toast.LENGTH_SHORT).show();

            }else {
                CommanMethod.showAlert(response.getString("responseMessage"),ProductDetailActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
