package com.ggboy.core.mapper;

import com.ggboy.core.domain.DO.BlogInfoDO;
import com.ggboy.core.domain.DO.BlogListDO;
import com.ggboy.core.domain.query.BlogListQuery;
import com.ggboy.core.domain.query.BlogShowQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper {
    String columns = "blog_id,title,synopsis";
    String for_update_columns = "blog_id,title,synopsis,cast(content as char CHARACTER SET utf8) as content,status,weight";
    String table = "blog";

    @SelectProvider(type = Provider.class, method = "queryBlogList")
    List<BlogListDO> selectList(BlogListQuery query);

    @SelectProvider(type = Provider.class, method = "queryForShow")
    BlogInfoDO selectForShow(BlogShowQuery query);

    @Select("select blog_id,title from " + table + " order by ${orderBy}")
    List<Map<String, Object>> selectSimpleList(@Param("orderBy") String orderBy);

    @Select("select blog_id,title,DATE_FORMAT(create_time,'%Y-%m-%d') as time from " + table + " order by create_time")
    List<Map<String, Object>> selectTimeLine();

    @Select("select " + columns + " from " + table + " order by ${orderBy} limit 1")
    Map<String, Object> selectTop(@Param("orderBy") String orderBy);

    @Update("update " + table + " SET view_count = view_count + 1 WHERE blog_id = #{blogId,jdbcType=VARCHAR}")
    Integer viewPlusOne(@Param("blogId") String blogId);

    @Update("update " + table + " SET favorite_count = favorite_count + 1 WHERE blog_id = #{blogId,jdbcType=VARCHAR}")
    Integer favoritePlusOne(@Param("blogId") String blogId);

    @Select("select " + for_update_columns + " from " + table + " where blog_id = #{blogId, jdbcType=VARCHAR}")
    Map<String, Object> selectForUpdate(@Param("blogId") String blogId);

    @UpdateProvider(type = Provider.class, method = "updateBlog")
    Integer update(Map<String, Object> params);
}