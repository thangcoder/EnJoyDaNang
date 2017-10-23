package node.com.enjoydanang.ui.fragment.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import node.com.enjoydanang.R;

public class PageFragment extends Fragment {

    private static String url;
    private SimpleDraweeView roundBorderImage;
    public static PageFragment getInstance(String url) {
        PageFragment f = new PageFragment();
        Bundle args = new Bundle();
        args.putString("image_source",url);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("image_source");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        roundBorderImage = (SimpleDraweeView) view.findViewById(R.id.full_custom_image);
        final Uri imageUri = Uri.parse(url);
        roundBorderImage.setImageURI(imageUri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
