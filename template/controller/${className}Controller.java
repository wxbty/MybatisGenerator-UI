<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<#assign shortName = table.shortName>
package ${basepackage}.${subpackage}.controller;

import java.util.*;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import ${basepackage}.${subpackage}.bean.DataMap;
import ${basepackage}.${subpackage}.bean.NewsConst;
import ${basepackage}.${subpackage}.bean.Status;
import ${basepackage}.${subpackage}.bean.StatusException;
import ${basepackage}.${subpackage}.device.talk.${className}Query;
import ${basepackage}.${subpackage}.device.web.vo.News${className}Vo;
import ${basepackage}.${subpackage}.help.core.Conf;
import ${basepackage}.${subpackage}.help.core.HttpJson;
import ${basepackage}.${subpackage}.help.core.LogResult;
import ${basepackage}.${subpackage}.help.core.Now;
import ${basepackage}.${subpackage}.help.util.Beans;
import ${basepackage}.${subpackage}.help.util.Gsons;
import ${basepackage}.${subpackage}.help.util.OssUtil;
import ${basepackage}.${subpackage}.help.util.PythonUtil;
import ${basepackage}.${subpackage}.service.repo.entity.News${className};
import ${basepackage}.${subpackage}.service.repo.mapper.News${className}Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @version 1.0
 * @author 
 */
@RestController
@RequestMapping("/${classNameLower}")
public class ${className}Controller  {

	@Resource
	private News${className}Mapper news${className}Mapper;

	@LogResult
	@HttpJson("/list")
	public Object list(@RequestBody JsonObject req) {
		${className}Query query = Gsons.fromJson(req, ${className}Query.class);
		query.build(Now::millis);

		DataMap<String, Object> result = DataMap.build();

		int total = news${className}Mapper.total(query.getId(), query.getTitle(), query.getBeginTime(), query.getEndTime());
		result.add("total", total);
		if (total == 0) {
			return result.add("${classNameLower}s", Lists.newArrayList());
		}
		List<News${className}> list = news${className}Mapper.list(query.getId(), query.getTitle(), query.getBeginTime(), query.getEndTime(), query.getCount(), query.getOffset());

		List<News${className}Vo> ${classNameLower}Vos = list.stream().map(${classNameLower} ->
		{
			News${className}Vo vo = Beans.copy(${classNameLower}, News${className}Vo.class);
			vo.setVoiceUrl(${classNameLower}.getExt());
			return vo;
		}).collect(Collectors.toList());
		return result.add("${classNameLower}s", ${classNameLower}Vos);

	}

	@LogResult
	@HttpJson("/add")
	public Object add(@RequestBody JsonObject req) {
		String title = Gsons.getString(req, "title");
		String content = Gsons.getString(req, "content");
		if (title == null || content == null) {
			throw new StatusException(Status.INVALID_ARGUMENT);
		}

		News${className} record = new News${className}();
		record.setTitle(title);
		record.setContent(content);
		record.setCreateTime(Now.millis());
		record.setUpdateTime(Now.millis());
		record.setCntime(DateUtil.now());
		record.setStatus(NewsConst.STATUS_ON);
		news${className}Mapper.insertSelective(record);

		if (Conf.getBoolean("oss.enable", true)) {
			PythonUtil.gen${className}Mp3(record.getId());
			String ${classNameLower}Path = Conf.getString("${classNameLower}_mp3_path", "/root/news_project/news-web/voice/mp3/${classNameLower}/xiaoyan/");
			String fileName = record.getId() + ".mp3";
			String url = OssUtil.upload(${classNameLower}Path, "${classNameLower}", fileName);
			record.setExt(url);
			news${className}Mapper.updateByPrimaryKeySelective(record);
		}
		return null;
	}


	@LogResult
	@HttpJson("/edit")
	public Object edit(@RequestBody JsonObject req) {
		Integer id = Gsons.getInt(req, "id");
		String title = Gsons.getString(req, "title");
		String content = Gsons.getString(req, "content");
		if (id == null || title == null || content == null) {
			throw new StatusException(Status.INVALID_ARGUMENT);
		}

		News${className} news${className} = news${className}Mapper.selectByPrimaryKey(id);
		if (news${className} == null) {
			throw new StatusException(Status.NOT_EXIST);
		}

		news${className}.setTitle(title);
		news${className}.setContent(content);

		news${className}Mapper.updateByPrimaryKeySelective(news${className});
		return null;
	}

	@LogResult
	@HttpJson("/del")
	public Object del(@RequestBody JsonObject req) {
		Integer id = Gsons.getInt(req, "id");

		if (id == null) {
			throw new StatusException(Status.INVALID_ARGUMENT);
		}

		News${className} news${className} = news${className}Mapper.selectByPrimaryKey(id);
		if (news${className} == null) {
			throw new StatusException(Status.NOT_EXIST);
		}

		news${className}Mapper.deleteByPrimaryKey(id);
		return null;
	}

}
