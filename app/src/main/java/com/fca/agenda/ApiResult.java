package com.fca.agenda;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by fabiano.alvarenga on 06/05/18.
 */

public interface ApiResult {

    void onResponse(Call call, Response response);

}
