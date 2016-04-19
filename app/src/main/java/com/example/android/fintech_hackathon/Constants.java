package com.example.android.fintech_hackathon;

/**
 * Created by giorgos on 4/19/2016.
 */
public class Constants {

    /**
     *  API Requests
     */
    static final String GET_PAYMENT_STATUS_URL =
            "https://zapit-web.herokuapp.com/api/v1/products/payment/status";
    static final String MAKE_PAYMENT_URL =
            "https://zapit-web.herokuapp.com/api/v1/products/payment/request";


    /**
     *  API Response Tags - Constants
     */
    // status_code
    static final String TAG_STATUS_CODE = "status_code";
    // data
    static final String TAG_DATA = "data";
    // slug
    static final String TAG_SLUG = "slug";
    // name
    static final String TAG_NAME = "name";
    // price
    static final String TAG_PRICE = "price";
    // description
    static final String TAG_DESCRIPTION = "description";
    // payed
    static final String TAG_PAYED = "payed";

    // error
    static final String TAG_ERROR = "error";
    // message
    static final String TAG_MESSAGE = "message";



}
