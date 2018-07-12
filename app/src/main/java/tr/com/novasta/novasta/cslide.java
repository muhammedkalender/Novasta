package tr.com.novasta.novasta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class cslide extends PagerAdapter{
    String images[];
    LayoutInflater layoutinflater;

    public cslide(String pimages[]){
        images = pimages;
        layoutinflater = (LayoutInflater) clib.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        View item = layoutinflater.inflate(R.layout.slide_item,container,false);
        clib.glide(images[position], (ImageView) item.findViewById(R.id.ivitem));
        container.addView(item);

        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
