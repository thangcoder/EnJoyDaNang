package node.com.enjoydanang.ui.fragment.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.widget.TouchImageView;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<PartnerAlbum> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private TouchImageView imgPhoto;


    public static SlideshowDialogFragment newInstance() {
        return new SlideshowDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        lblCount = (TextView) view.findViewById(R.id.lbl_count);
        lblTitle = (TextView) view.findViewById(R.id.title);
        lblDate = (TextView) view.findViewById(R.id.date);
        images = (ArrayList<PartnerAlbum>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return view;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
            if(imgPhoto != null){
                myViewPagerAdapter.notifyDataSetChanged();
//                imgPhoto.reiniciarZoom();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        PartnerAlbum model = images.get(position);
        lblTitle.setText(model.getTitle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            imgPhoto = (TouchImageView) view.findViewById(R.id.image_preview);
            DisplayMetrics display = getActivity().getResources().getDisplayMetrics();
            int width = display.widthPixels;
            int height = ((display.heightPixels * 75) / 100);
            RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width, height);
            parms.addRule(RelativeLayout.CENTER_VERTICAL);
            imgPhoto.setLayoutParams(parms);
            PartnerAlbum model = images.get(position);
            Glide.with(getActivity()).load(model.getPicture())
                    .asBitmap()
                    .thumbnail(0.5f)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            imgPhoto.setImageBitmap(resource);
                        }
                    });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
