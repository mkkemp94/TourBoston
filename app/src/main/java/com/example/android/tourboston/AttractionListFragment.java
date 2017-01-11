package com.example.android.tourboston;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static com.example.android.tourboston.ExplorationActivity.attractionListFood;
import static com.example.android.tourboston.ExplorationActivity.attractionListPlaces;
import static com.example.android.tourboston.ExplorationActivity.attractionListSchools;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionListFragment extends Fragment {

    // The view holding the attraction list
    private View rootView;

    // List view for putting the attraction list into
    private ListView listView;

    public static Attraction currentFoodAttraction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Define the view as being a list
        RecyclerView recyclerView = (RecyclerView) inflater.inflate
                (R.layout.fragment_attraction_list, container, false);

        // Get the list for this page
        List<Attraction> thisAttractionList = getAttractionList();

        // Set the adapter
        recyclerView.setAdapter(new AttractionListAdapter(getActivity(), thisAttractionList));

        // Set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        return recyclerView;
    }

    /**
     * Gets the list for this particular fragment
     * from ExplorationActivity
     * @return
     */
    public List<Attraction> getAttractionList() {

        String listType = getArguments().getString("List");
        switch (listType) {
            case "Food" :
                Log.v("listType", "Found foods");
                return attractionListFood;
            case "Schools" :
                Log.v("listType", "Found schools");
                return attractionListSchools;
            case "Places" :
                Log.v("listType", "Found places");
                return attractionListPlaces;
            default:
                Log.v("listType", "Found Nothing");
                return null;
        }

    }

    /**
     * Class for recycler view adapter
     */
    public static class AttractionListAdapter
            extends RecyclerView.Adapter<AttractionListAdapter.ViewHolder> {

        // Variables
        private final TypedValue mTypedValue = new TypedValue();
        private List<Attraction> mValues;
        private int mBackground;

        // Constructor
        public AttractionListAdapter(Context context, List<Attraction> list) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = list;
        }

        // Create a new ViewHolder
        @Override
        public AttractionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        // Fill in fields
        @Override
        public void onBindViewHolder(final AttractionListAdapter.ViewHolder holder, final int position) {
            holder.mBoundString = mValues.get(position).toString();
            holder.mTextView.setText(mValues.get(position).getAttractionName());
            holder.mImageView.setImageResource(mValues.get(position).getImageResource());

            // Go to description activity
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AttractionDetailsActivity.class);
                    intent.putExtra(AttractionDetailsActivity.EXTRA_NAME, mValues.get(position).getAttractionName());
                    intent.putExtra(AttractionDetailsActivity.EXTRA_DESCRIPTION, mValues.get(position).getDescription());
                    if (mValues.get(position).getBackgroundResource() != -1) {
                        intent.putExtra(AttractionDetailsActivity.EXTRA_IMAGE, mValues.get(position).getBackgroundResource());
                    } else {
                        intent.putExtra(AttractionDetailsActivity.EXTRA_IMAGE, mValues.get(position).getImageResource());
                    }

                    context.startActivity(intent);
                }
            });

            // Load image with glide
//            Glide.with(holder.mImageView.getContext())
//                    .load(mValues.get(position).getImageResource())
//                    .fitCenter()
//                    .into(holder.mImageView);
        }

        // Get item count
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        // The View Holder
        public static class ViewHolder extends RecyclerView.ViewHolder {

            // Variables
            public String mBoundString;
            public final View mView;
            public final TextView mTextView;
            public final ImageView mImageView;

            // Constructor
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(R.id.text);
                mImageView = (ImageView) view.findViewById(R.id.image);
            }
        }
    }
}
