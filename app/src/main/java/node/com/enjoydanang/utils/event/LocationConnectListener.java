package node.com.enjoydanang.utils.event;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Author: Tavv
 * Created on 16/12/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface LocationConnectListener extends GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener  {
}
