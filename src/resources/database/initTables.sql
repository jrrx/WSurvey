--问卷表
CREATE TABLE `t_paper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `paperstatus` int(11) DEFAULT NULL,
  `papertype` int(11) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `lastmodifytime` timestamp NULL DEFAULT NULL,
  `authorid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--问题表
CREATE TABLE `t_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(256) DEFAULT NULL,
  `paperid` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--答案表
CREATE TABLE `t_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `questionid` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `content` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--粉丝回答记录表
CREATE TABLE `t_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paperid` int(11) DEFAULT NULL,
  `fanid` int(11) DEFAULT NULL,
  `questionpos` int(11) DEFAULT NULL,
  `answertext` varchar(128) DEFAULT NULL,
  `answertime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--问卷和二维码对应关系表
CREATE TABLE `t_qrcode_paper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sceneid` int(11) DEFAULT NULL,
  `paperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--公众号微信粉丝表
CREATE TABLE `t_fan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(128) DEFAULT NULL,
  `fanstatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--系统用户信息表
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `nickname` varchar(32) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `lastmodifytime` timestamp NULL DEFAULT NULL,
  `lastlogintime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
