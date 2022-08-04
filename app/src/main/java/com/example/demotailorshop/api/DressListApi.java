package com.example.demotailorshop.api;

import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressDetail;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DressListApi {
    //   @get url     localhost:8081/api/v1/dress/allDress
//    Call the api to get the dress list based on filter and with paging and sorting
    @GET(value = "api/dress/v1/allDress/{userId}/{offset}/{limit}")
    Call<ApiResponse<List<Dress>>> getAllDress(@HeaderMap Map<String, String> headers,
                                               @Path(value = "userId") Integer userId, @Path(value = "offset") Integer offset,
                                               @Path(value = "limit") Integer limit, @Query(value = "dressType") List<String> dressTypes);

    @GET(value = "api/dress/v1/allDress/dressDetail/{userId}/{customerId}")
    Call<ApiResponse<DressDetail>> getDressDetails(@HeaderMap Map<String, String> headers, @Path(value = "userId") Integer userId, @Path("customerId") int customerId);

    @POST(value = "api/dress/v1/allDress/updateDress/{userId}")
    Call<ApiResponse<DressDetail>> updateDressDetail(@HeaderMap Map<String, String> headers, @Path(value = "userId") Integer userId, @Body DressDetail dressDetail);

    @Multipart
    @POST(value = "api/dress/v1/allDress/updateDress/updateImages/{userId}/{customerId}")
    Call<ApiResponse<DressDetail>> updateDressImagesNew(@HeaderMap Map<String, String> headers,
                                                        @Path(value = "userId") Integer userId, @Path(value = "customerId") Integer customerId, @Part List<MultipartBody.Part> files);
}
