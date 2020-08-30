package com.axxess.cavista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment implements ImageItemClickListener {


    private static final String IMAGE_SEARCH_API = "https://api.imgur.com/3/gallery/search/1?q=";
    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String AUTHORIZATION_VALUE = "Client-ID 137cda6b5008a7c";

    private String mSearchQuery;

    private RecyclerView mImageGrid;
    private List<ImageModel> mImageModelList;
    private ImageAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of
     * this fragment using the provided parameters.
     *
     * @param searchQuery .
     * @return A new instance of fragment SearchResultsFragment.
     */
    public static SearchResultsFragment newInstance(String searchQuery) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(BaseActivity.SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchQuery = getArguments().getString(BaseActivity.SEARCH_QUERY);

            callSearchApi(mSearchQuery);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_results_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeRecyclerView(view);

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Initialize the Image RecyclerView and its adapter
     * @param view
     */
    private void initializeRecyclerView(View view) {
        mImageGrid = view.findViewById(R.id.rv_images);

        mImageModelList = new ArrayList<>();

        int numberOfColumns = 3;
        mImageGrid.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        mImageGrid.setHasFixedSize(true);

    }

    /**
     * Call the API for search and handle the response
     * @param query
     */
    private void callSearchApi(String query) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.loading_images));
        mProgressDialog.show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = IMAGE_SEARCH_API + query;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response != null) {
                            parseResponse(response);
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.dismiss();
                toggleErrorTextVisibility(View.VISIBLE, R.string.error_loading_images);
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(AUTHORIZATION_KEY, AUTHORIZATION_VALUE);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Set text and toggle visibility of textview
     * @param visible
     * @param textRes
     */
    private void toggleErrorTextVisibility(int visible, int textRes) {
        TextView tvError = getView().findViewById(R.id.tv_error_msg);
        if(tvError != null) {
            tvError.setText(textRes);
            tvError.setVisibility(visible);
        }
    }

    /**
     * Parse the JSON response into ImageModelList
     * @param response JSON response
     */
    private void parseResponse(String response) {
        JSONObject json = null;
        mImageModelList = new ArrayList<ImageModel>();
        try {
            json = new JSONObject(response);
            if(json != null ) {
                JSONArray array = json.getJSONArray("data");

                if(array != null && array.length() > 0) {
                    Log.i("INFO", "SearchResultsFragment: Array length" + array.length());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject currentObj = array.getJSONObject(i);
                        boolean isAlbum = currentObj.getBoolean(ImageModel.IS_ALBUM_KEY);

                        if (isAlbum) {
                            //If the current model is an Album, parse the images in that album.
                            JSONArray imagesInAlbum = currentObj.getJSONArray(ImageModel.IMAGES_KEY);
                            String albumTitle = currentObj.getString(ImageModel.IMAGE_TITLE_KEY);
                            if(imagesInAlbum != null) {
                                for (int j = 0; j < imagesInAlbum.length(); j++) {
                                    JSONObject currentImageInAlbum = imagesInAlbum.getJSONObject(j);

                                    ImageModel image = new ImageModel();
                                    image.setImageId(currentImageInAlbum.getString(ImageModel.IMAGE_ID_KEY));
                                    image.setImageUrl(currentImageInAlbum.getString(ImageModel.IMAGE_URL_KEY));

                                    String title = currentImageInAlbum.getString(ImageModel.IMAGE_TITLE_KEY);
                                    if(title == null || title.equals("null") || title.isEmpty()) {
                                        image.setImageTitle(albumTitle);
                                    } else {
                                        image.setImageTitle(title);
                                    }

                                    mImageModelList.add(image);
                                }
                            }
                        } else {
                            ImageModel image = new ImageModel();
                            image.setImageId(currentObj.getString(ImageModel.IMAGE_ID_KEY));
                            image.setImageUrl(currentObj.getString(ImageModel.IMAGE_URL_KEY));
                            image.setImageTitle(currentObj.getString(ImageModel.IMAGE_TITLE_KEY));

                            mImageModelList.add(image);
                        }

                    }

                    mAdapter = new ImageAdapter(getActivity(), mImageModelList, this);
                    mImageGrid.setAdapter(mAdapter);

                    mProgressDialog.dismiss();

                    Log.i("INFO", "SearchResultsFragment: Image list size" + mImageModelList.size());
                } else {
                    mProgressDialog.dismiss();
                    toggleErrorTextVisibility(View.VISIBLE, R.string.no_results);
                }
            } else {
                mProgressDialog.dismiss();
                toggleErrorTextVisibility(View.VISIBLE, R.string.error_loading_images);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(View view, ImageModel image) {
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra(ImageModel.IMAGE_ID_KEY, image.getImageId());
        intent.putExtra(ImageModel.IMAGE_URL_KEY, image.getImageUrl());
        intent.putExtra(ImageModel.IMAGE_TITLE_KEY, image.getImageTitle());
        startActivity(intent);
    }
}