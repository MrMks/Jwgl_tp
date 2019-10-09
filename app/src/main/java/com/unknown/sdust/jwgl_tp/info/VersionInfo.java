package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.data.VersionData;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;

public class VersionInfo extends JsonInfo<VersionData>{
    @Override
    String getChild() {
        return JsonRes.versionFile;
    }

    @Override
    Class<VersionData> getKlass() {
        return VersionData.class;
    }
}
