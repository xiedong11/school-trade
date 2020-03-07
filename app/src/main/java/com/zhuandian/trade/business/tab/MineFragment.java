package com.zhuandian.trade.business.tab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.trade.R;
import com.zhuandian.trade.business.goods.MyCollectionGoodsActivity;
import com.zhuandian.trade.business.goods.MyCommentGoodsActivity;
import com.zhuandian.trade.business.goods.MyReleaseGoodsActivity;
import com.zhuandian.trade.business.goods.MyShopingCarActivity;
import com.zhuandian.trade.utils.PictureSelectorUtils;
import com.zhuandian.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc :
 * author：xiedong
 * date：2020/03/03
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.iv_header, R.id.tv_nick_name, R.id.tv_my_release, R.id.tv_my_comment, R.id.tv_my_collect, R.id.tv_more_setting, R.id.tv_logout,R.id.tv_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 1);
                break;
            case R.id.tv_nick_name:
                break;
            case R.id.tv_my_release:
                startActivity(new Intent(actitity, MyReleaseGoodsActivity.class));
                break;
            case R.id.tv_my_comment:
                startActivity(new Intent(actitity, MyCommentGoodsActivity.class));
                break;
            case R.id.tv_my_collect:
                startActivity(new Intent(actitity, MyCollectionGoodsActivity.class));
                break;
            case R.id.tv_more_setting:
                break;
            case R.id.tv_logout:
                break;
            case R.id.tv_car:
                startActivity(new Intent(actitity, MyShopingCarActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    String imagePath = selectList.get(0).getCompressPath();
                    decodePath2Bitmap(imagePath);
                    //上传文件
                }
            }
        }
    }

    /**
     * 把指定路径的image资源转成Bitmap
     *
     * @param path
     */
    private void decodePath2Bitmap(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm != null) {
            ivHeader.setImageBitmap(bm);
        }
    }

}
