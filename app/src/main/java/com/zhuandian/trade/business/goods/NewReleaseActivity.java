package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.PictureSelectorUtils;
import com.zhuandian.trade.view.SettingItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class NewReleaseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_photo_preview)
    LinearLayout llPhotoPreview;
    @BindView(R.id.rl_image_container)
    RelativeLayout rlImageContainer;
    @BindView(R.id.ed_goods_title)
    EditText edGoodsTitle;
    @BindView(R.id.ed_goods_content)
    EditText edGoodsContent;
    @BindView(R.id.cb_trade_type)
    CheckBox cbTradeType;
    @BindView(R.id.siv_goods_type)
    SettingItemView sivGoodsType;
    @BindView(R.id.ed_goods_price)
    EditText edGoodsPrice;
    @BindView(R.id.ed_goods_owner_contact_num)
    EditText edGoodsOwnerContactNum;
    @BindView(R.id.ll_photo_container)
    LinearLayout llPhotoContainer;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    @BindView(R.id.ll_root_view)
    LinearLayout llRootView;
    @BindView(R.id.iv_priview_img_1)
    ImageView ivPriviewImg1;
    @BindView(R.id.iv_priview_img_2)
    ImageView ivPriviewImg2;
    @BindView(R.id.iv_priview_img_3)
    ImageView ivPriviewImg3;
    private List<String> goodsUrl = new ArrayList<>();
    private ImageView[] goodsPreviewImg;//上传商品图片本地预览视图
    private int goodsType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_release;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("发布商品");
        goodsPreviewImg = new ImageView[]{ivPriviewImg1, ivPriviewImg2, ivPriviewImg3};
        sivGoodsType.setItemClickListener(() -> choseGoodsType());
    }



    @OnClick({R.id.iv_back, R.id.ll_photo_preview, R.id.tv_release})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_photo_preview:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 3);
                break;
            case R.id.tv_release:
                releaseGoodsInfo();
                break;
        }
    }

    private void choseGoodsType() {
        final String typeArray[] = new String[]{"日常用品", "杂七杂八", "电子产品", "书籍资料", "美妆洗化"};
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("商品类型")
                .setSingleChoiceItems(typeArray,
                        0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        sivGoodsType.setRightText(typeArray[0]);
                                        break;
                                    case 1:
                                        sivGoodsType.setRightText(typeArray[1]);
                                        break;
                                    case 2:
                                        sivGoodsType.setRightText(typeArray[2]);
                                        break;
                                    case 3:
                                        sivGoodsType.setRightText(typeArray[3]);
                                        break;
                                    case 4:
                                        sivGoodsType.setRightText(typeArray[4]);
                                        break;
                                }
                                goodsType = which;
                                dialog.cancel();
                            }
                        }
                ).setPositiveButton("取消", null)  //确定事件的监听事件设为空
                .create()
                .show();

    }

    private void releaseGoodsInfo() {
        String goodsContent = edGoodsContent.getText().toString();
        String goodsTitle = edGoodsTitle.getText().toString();
        String goodsPrice = edGoodsPrice.getText().toString();
        String goodsOwnerContactNum = edGoodsOwnerContactNum.getText().toString();
        int goodsTradeType = cbTradeType.isChecked() ? GoodsEntity.TRADE_TYPE_BARGAIN : GoodsEntity.TRADE_TYPE_NO_BARGAIN;
        UserEntity currentUser = BmobUser.getCurrentUser(UserEntity.class);

        //校验信息是否完善
        if (goodsContent.equals("") || goodsTitle.equals("") || goodsPrice.equals("") || goodsUrl.size() == 0 || goodsOwnerContactNum.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("请完善信息")
                    .setMessage("为了物品尽快找到新主人，请完善所有发布项...")
                    .show();
            return;
        }

        //校验用户登录是否有效
        if (currentUser == null) {
            new AlertDialog.Builder(this)
                    .setTitle("登录过期")
                    .setMessage("无法同步你的登录信息，请注销后重新登录...")
                    .show();
            return;
        }

        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoodsOwner(currentUser);
        goodsEntity.setGoodsContent(goodsContent);
        goodsEntity.setGoodsUrl(goodsUrl);
        goodsEntity.setGoodsTitle(goodsTitle);
        goodsEntity.setOwnerContactNum(goodsOwnerContactNum);
        goodsEntity.setTradeState(GoodsEntity.GOODS_STATE_ON_SALE);
        goodsEntity.setGoodsPrice(goodsPrice);
        goodsEntity.setTradeType(goodsTradeType);
        goodsEntity.setGoodsType(goodsType);
        goodsEntity.setGoodsLocal(currentUser.getUserSchool());
        goodsEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(NewReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    NewReleaseActivity.this.finish();
                } else {
                    Toast.makeText(NewReleaseActivity.this, "发布失败，请重试...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList.size() > 0) {
                llPhotoPreview.setVisibility(View.GONE); //隐藏上传商品图片提示
                String[] imgs = new String[selectList.size()];
                for (int i = 0; i < selectList.size(); i++) {
                    imgs[i]= selectList.get(i).getPath();
                    goodsPreviewImg[i].setImageURI(Uri.parse(selectList.get(i).getPath()));
                }

                BmobFile.uploadBatch(imgs, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> urls) {
                        goodsUrl = urls;
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        System.out.println(s+"-----------------------");
                    }
                });
            }

        }
    }
}
