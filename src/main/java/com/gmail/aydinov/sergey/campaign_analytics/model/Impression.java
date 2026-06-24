package com.gmail.aydinov.sergey.campaign_analytics.model;

import java.time.LocalDate;

public record Impression(
	    String uid,
	    LocalDate regTime,
	    int fcImpChk,
	    int fcTimeChk,
	    int utmtr,
	    String mmDma,
	    String osName,
	    String model,
	    String hardware,
	    String siteId
	) {}