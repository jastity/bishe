package cn.roothub.web.front;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import cn.roothub.config.SiteConfig;
import cn.roothub.config.service.RedisService;
import cn.roothub.entity.User;
import cn.roothub.service.CollectService;
import cn.roothub.service.NoticeService;
import cn.roothub.service.ReplyService;
import cn.roothub.service.NodeTabService;
import cn.roothub.service.TopicService;
import cn.roothub.service.UserService;
import cn.roothub.util.Base64Util;
import cn.roothub.util.CookieAndSessionUtil;
import cn.roothub.util.JsonUtil;

@Controller
public class BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService rootUserService;
	@Autowired
	private TopicService rootTopicService;
	@Autowired
	private NodeTabService rootSectionService;
	@Autowired
	private NoticeService rootNoticeService;
	@Autowired
	private ReplyService rootReplyService;
	@Autowired
	private CollectService collectDaoService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private RedisService redisService;

	/**
	 * 获取登录用户的信息
	 *
	 * @param request
	 * @return
	 */
	public User getUser(HttpServletRequest request) {
		return CookieAndSessionUtil.getSession(request, "user");
	}

	public String isLogin(HttpServletRequest request, String errorPage, String suesscePage) {
		User user = getUser(request);
		if (user == null) {
			return errorPage;
		}
		return suesscePage;
	}

	/**
	 * 未读通知的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getNotReadNotice(HttpServletRequest request) {
		int notReadNotice = 0;
		notReadNotice = rootNoticeService.countNotReadNotice(getUser(request).getUserName());
		return notReadNotice;
	}

	/**
	 * 发布的主题的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getCountTopicByUserName(HttpServletRequest request) {
		int countTopicByUserName = 0;
		countTopicByUserName = rootTopicService.countByUserName(getUser(request).getUserName());
		return countTopicByUserName;
	}

	/**
	 * 收藏话题的数量
	 * 
	 * @param request
	 * @return
	 */
	public int getCountCollect(HttpServletRequest request) {
		int countCollect = 0;
		collectDaoService.count(getUser(request).getUserId());
		return countCollect;
	}
}
