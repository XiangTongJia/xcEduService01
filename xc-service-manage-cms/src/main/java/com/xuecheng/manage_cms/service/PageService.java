package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomCast;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    public QueryResponseResult findList(int page, int size, QueryPageRequest pageRequest) {

        if(pageRequest == null){
            pageRequest = new QueryPageRequest();
        }

        if(page<=0){
            page=1;
        }
        page=page-1;
        if(size<=0){
            size=10;
        }
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotEmpty(pageRequest.getSiteId())){
            cmsPage.setSiteId(pageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(pageRequest.getPageAliase())){
            cmsPage.setPageAliase(pageRequest.getPageAliase());
        }
        if(StringUtils.isNotEmpty(pageRequest.getTemplateId())){
            cmsPage.setTemplateId(pageRequest.getTemplateId());
        }


        Pageable pageable = PageRequest.of(page,size);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CmsPage> example =Example.of(cmsPage,exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;
    }



    public CmsPageResult add(CmsPage cmsPage){
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
         //捕获异常
        if(cmsPage1!=null){
           CustomCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
       }

        if(cmsPage1==null){
             cmsPage.setPageId(null);
            CmsPage save = cmsPageRepository.save(cmsPage);
            return  new CmsPageResult(CommonCode.SUCCESS,save);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public CmsPage getById(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if(byId.isPresent()){
            CmsPage cmsPage = byId.get();
            return  cmsPage;
        }
        return null;
    }
    public CmsPageResult update(String id,CmsPage cmsPage){
        CmsPage one = this.getById(id);
        if (one != null) {


            one.setTemplateId(cmsPage.getTemplateId());
//更新所属站点
            one.setSiteId(cmsPage.getSiteId());
//更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
//更新页面名称
            one.setPageName(cmsPage.getPageName());
//更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
//更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setDataUrl(cmsPage.getDataUrl());
//执行更新
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
//返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }

        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public ResponseResult delete(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if(byId.isPresent()){
           cmsPageRepository.deleteById(id);
           ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
           return  responseResult;
        }
        return null;
    }

}
