package person.lgc.myexperiencecode.listViewSeries;/*
package a.baozouptu.chosePicture;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import a.baozouptu.R;
import a.baozouptu.base.dataAndLogic.AllData;
import a.baozouptu.base.dataAndLogic.AsyncImageLoader3;
import a.baozouptu.base.util.FileTool;
import a.baozouptu.base.util.Util;
import a.baozouptu.ptu.PtuActivity;



*/
/*
 * 显示所选的最近的或某个文件夹下面的所有图片
 * 并且有选择文件夹，相机，空白图画图的功能
 *//*


public class ChosePictureActivityBackupBackup extends AppCompatActivity {
    private String TAG = "ChosePictureActivityBackup";


*/
/*
     * 进度条
    *//*


    private ProgressDialog m_ProgressDialog = null;


*/
/*
     * 保存最近图片的路径
      *//*


    public static List<String> usualyPicPathList = new ArrayList<>();


*/
/*
     * 获取和保存某个文件下面所有图片的路径
      *//*


    private List<String> picPathInFile = new ArrayList<>();


*/
/*
     * 当前要现实的所有图片的路径
      *//*


    private List<String> currentPicPathList = new ArrayList<>();



*/
/*     * 文件列表的几个相关list

 *//*

    private List<String> dirInfoList, dirPathList, dirRepresentPathList;

    private DrawerLayout fileListDrawer;
    private GridViewAdapter picAdpter;
    private GridView pictureGridview;
    private ProcessUsuallyPicPath usuPicProcess;

    private ListView pictureFileListView;
    private MyFileListAdapter fileAdapter;
    boolean isFirst = true;



*/
/*     * Called when the activity is first created.
     * 过程描述：启动一个线程获取所有图片的路径，再启动一个子线程设置好GridView，而且要求这个子线程必须在ui线程之前启动
      *//*


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chose_picture);
        //test();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.equals("change_pic")) {
                    if (isFirst) {
                        pictureGridview.setAdapter(picAdpter);
                        m_ProgressDialog.dismiss();// 表示此处开始就解除这个进度条Dialog，应该是在相对起始线程的另一个中使用
                        isFirst = false;
                    } else {
                        picAdpter.notifyDataSetChanged();
                    }
                    Util.P.le(TAG, "finish update picture");
                } else if (msg.obj.equals("change_file")) {
                    Util.P.le(TAG, "finish update file");
                    fileAdapter.notifyDataSetChanged();
                }
            }
        };
        usuPicProcess = new ProcessUsuallyPicPath(this, handler);
        getScreenWidth();
        initView();
        initToolbar();
        m_ProgressDialog = ProgressDialog.show(ChosePictureActivityBackup.this, "请稍后",
                "数据读取中...", true);
        initPicInfo();
    }



    private void test() {
        if (getIntent().getStringExtra("test") == null) return;
        Intent intent1 = new Intent(this, PtuActivity.class);
        intent1.putExtras(getIntent());
        intent1.putExtra("pic_path", "/storage/sdcard1/中大图.jpg");
        startActivityForResult(intent1, 0);
    }



*/
/*     * 获取所有图片的文件信息，最近的图片，
     * <p>并且为图片grid，文件列表加载数据
      *//*


    private void initPicInfo() {
        usualyPicPathList = usuPicProcess.getUsuallyPathFromDB();
        currentPicPathList = usualyPicPathList;
        Util.P.le(TAG, "获取了上次数据库中的图片");
        disposeShowPicture();
        Util.P.le(TAG, "初始化显示图片完成");
        disposeDrawer();
        Util.P.le(TAG, "初始化显示Drawer完成");
    }



*/
/*     * 获取屏幕的宽度
      *//*


    void getScreenWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        AllData.screenWidth = metric.widthPixels; // 屏幕宽度（像素）
    }

    Toolbar toolbar;

    private void initView() {
        fileListDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_show_picture);
        pictureGridview = (GridView) findViewById(R.id.gv_photolist);
        toolbar = (Toolbar) findViewById(R.id.toolbar_show_picture);
        setSupportActionBar(toolbar);
        final ImageButton showFile = (ImageButton) findViewById(R.id.show_pic_file);
        showFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileListDrawer.isDrawerOpen(GravityCompat.END))
                    fileListDrawer.closeDrawer(GravityCompat.END);
                else
                    fileListDrawer.openDrawer(GravityCompat.END);
            }
        });
    }

    @TargetApi(19)
    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Util.getColor(this, R.color.base_toolbar_background));
            tintManager.setStatusBarTintEnabled(true);
        }
    }



*/
/*     * 为显示图片的gridView加载数据

 *//*

    private void disposeShowPicture() {
        picAdpter = new GridViewAdapter(
                ChosePictureActivityBackup.this, currentPicPathList);
        pictureGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent sourceIntent = getIntent();
                if (sourceIntent != null) {
                    String s = sourceIntent.getAction();
                    if (s != null && s.equals("tietu")) {//是来自选择贴图，不是选择的贴图
                        Intent intent1 = new Intent();
                        Util.P.le(TAG, currentPicPathList.get(position));
                        intent1.putExtra("pic_path", currentPicPathList.get(position));
                        setResult(3, intent1);
                        ChosePictureActivityBackup.this.finish();
                    } else {//正常的选择
                        Intent intent = new Intent(ChosePictureActivityBackup.this, PtuActivity.class);
                        intent.putExtra("pic_path", currentPicPathList.get(position));
                        startActivityForResult(intent, 0);
                    }
                }
            }
        });

        pictureGridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            AsyncImageLoader3 imageLoader = AsyncImageLoader3.getInstatnce();

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        showAdjacentPic();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        imageLoader.cancelLoad();//取消解析，提交的任务还没有执行的就不执行了
                        break;
                }
            }

            AsyncImageLoader3.ImageCallback imageCallback = new AsyncImageLoader3.ImageCallback() {
                public void imageLoaded(Bitmap imageBitmap, ImageView image, int position, String imageUrl) {
                    if (image != null && position == (int) image.getTag()) {
                        if (imageBitmap == null) {
                            image.setImageResource(R.mipmap.decode_failed_icon);
                        } else
                            image.setImageBitmap(imageBitmap);
                    }
                }
            };

            private void showAdjacentPic() {
                int first = pictureGridview.getFirstVisiblePosition();
                int last = pictureGridview.getLastVisiblePosition();
                for (int position = first; position <= last; position++) {
                    if (position > currentPicPathList.size()) break;
                    String path = currentPicPathList.get(position);
                    final ImageView ivImage = (ImageView) pictureGridview.findViewWithTag(position);
                    imageLoader.loadBitmap(path, ivImage, position, imageCallback,
                            AllData.screenWidth / 3);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        pictureGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupWindow popWindowFile = new PopupWindow(ChosePictureActivityBackup.this);
                LinearLayout linearLayout = new LinearLayout(ChosePictureActivityBackup.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setDividerPadding(10);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(Util.getDrawable(R.drawable.divider_picture_opration));
                linearLayout.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT));
                linearLayout.setPadding(Util.dp2Px(2), Util.dp2Px(2), Util.dp2Px(2), Util.dp2Px(2));
                TextView frequentlyTextView = new TextView(ChosePictureActivityBackup.this);
                frequentlyTextView.setGravity(Gravity.CENTER);
                frequentlyTextView.setWidth(view.getWidth() / 2);
                frequentlyTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                final String path = currentPicPathList.get(position);
                if (usualyPicPathList.lastIndexOf(path) >= usuPicProcess.getPreferStart()) {
                    frequentlyTextView.setText("取消");
                    frequentlyTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            usuPicProcess.deletePreferPath(path, position);
                            dirInfoList.remove(0);
                            dirInfoList.add(0, "  " + "常用图片(" + usualyPicPathList.size() + ")");
                            if (currentPicPathList == usualyPicPathList)
                                picAdpter.notifyDataSetChanged();
                            popWindowFile.dismiss();
                        }
                    });
                } else {
                    frequentlyTextView.setText("常用");
                    frequentlyTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindowFile.dismiss();
                            boolean change = usuPicProcess.addPreferPath(path);
                            dirInfoList.remove(0);
                            dirInfoList.add(0, "  " + "常用图片(" + usualyPicPathList.size() + ")");
                            if (change && currentPicPathList == usualyPicPathList)
                                picAdpter.notifyDataSetChanged();
                        }
                    });

                }
                frequentlyTextView.setTextSize(22);
                frequentlyTextView.setTextColor(Util.getColor(R.color.text_deep_black));

                linearLayout.addView(frequentlyTextView);

                TextView deleteTextView = new TextView(ChosePictureActivityBackup.this);

                deleteTextView.setGravity(Gravity.CENTER);
                deleteTextView.setWidth(view.getWidth() / 2);
                deleteTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                deleteTextView.setText("删除");
                deleteTextView.setTextSize(22);
                deleteTextView.setTextColor(Util.getColor(R.color.text_deep_black));

                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWindowFile.dismiss();
                        deletePicture(currentPicPathList.get(position));
                    }
                });
                linearLayout.addView(deleteTextView);


                int[] popWH = new int[2];
                Util.getMesureWH(linearLayout, popWH);
                popWindowFile.setContentView(linearLayout);
                popWindowFile.setWidth(view.getWidth());
                popWindowFile.setHeight(popWH[1]);
                popWindowFile.setFocusable(true);
                popWindowFile.setBackgroundDrawable(Util.getDrawable(
                        R.drawable.background_pic_operation));
                popWindowFile.showAsDropDown(view, 0, -view.getHeight());
                return true;
            }
        });

    }

    private void deletePicture(final String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog alertDialog = builder.setTitle("删除此图片(包括SD卡中)")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        先从文件中删除，不能删除则不行
                        if (!usuPicProcess.onDeleteOnePicInfile(path))//删除图片文件并更新目录列表信息
                        {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(ChosePictureActivityBackup.this)
                                    .setTitle("删除失败，此图片无法删除")
                                    .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .create();
                            alertDialog1.show();
                            return;
                        }
                        if (usualyPicPathList.contains(path)) {//包含才常用列表里面，删除常用列表中的信息
                        }
                        picAdpter.notifyDataSetChanged();
                        fileAdapter.notifyDataSetChanged();
                    }
                })
                .create();
        alertDialog.show();
    }



    @Override
    protected void onResume() {
        usuPicProcess.getAllPicInfoAndRecent();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Util.P.le(TAG, "onActivityResult:开始处理其它activity的返回");
        if (resultCode == 0 && data != null) {
            String action = data.getAction();
            if (action != null && action.equals("finish")) {
                setResult(0, new Intent(action));
                finish();
                overridePendingTransition(0, R.anim.go_send_exit);
            } else {
                String path = data.getStringExtra("pic_path");
                if (path != null) {
                    usuPicProcess.addUsedPath(path);
                }
                if (path != null && currentPicPathList == usualyPicPathList) {
                    picAdpter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        AsyncImageLoader3.getInstatnce().evitAll();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (currentPicPathList == usualyPicPathList) {
            super.onBackPressed();
        } else {
            currentPicPathList = usualyPicPathList;
            picAdpter.setList(usualyPicPathList);
            pictureGridview.setAdapter(picAdpter);
        }
    }

    @Override
    protected void onDestroy() {
        AllData.lastScanTime = 0;
        super.onDestroy();
    }
}

*/
