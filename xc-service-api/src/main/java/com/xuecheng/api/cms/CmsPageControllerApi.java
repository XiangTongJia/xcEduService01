package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
                    @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
                    })
            public QueryResponseResult findList(int page, int size, QueryPageRequest pageRequest);

    @ApiImplicitParams({
            @ApiImplicitParam(name="cmsPage",value = "对象",required=true),

    })
    public CmsPageResult add(CmsPage cmsPage);
   @ApiOperation("修改页面")
    public CmsPageResult update(CmsPage cmsPage,String id);
     @ApiOperation("获取页面信息")
    public CmsPage get(String id);
     @ApiOperation("获取页面信息")
    public ResponseResult delete(String id);


}
