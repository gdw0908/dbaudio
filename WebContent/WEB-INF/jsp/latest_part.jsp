<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="dtf" uri="/WEB-INF/tlds/DateUtil_fn.tld"%>
<%@ taglib prefix="suf" uri="/WEB-INF/tlds/StringUtil_fn.tld"%>
<c:set var="totalpage"
	value="${data.pagination.totalpage>20?20:data.pagination.totalpage }" />
<c:set var="cpage" value="${empty param.cpage?'1':param.cpage }" />
<c:set var="prev" value="${cpage-1<=0?totalpage:cpage-1 }" />
<c:set var="next" value="${cpage+1>totalpage?1:cpage+1 }" />
<!-- brand list -->
<div class="brand_roll">
	<div class="sample_bn">
		<div>
			<ul>
				<!-- brand roll_1 -->
				<li>
					<div class="md_list brand_list">
						<ul>
							<c:forEach var="item" items="${data.list }">
								<li>
									<div class="md_top">
										<p class="mt_img">
											<a
												href="/goods/view.do?menu=menu${fn:substring(item.part1, 8, 9) }&seq=${item.item_seq }"><img
												src="${item.thumb }" alt="">
											</a>
										</p>
										<p class="mt_btn">
											<a
												href="/goods/view.do?menu=menu${fn:substring(item.part1, 8, 9) }&seq=${item.item_seq }"
												target="_blank"><img
												src="/images/container/md_img_btn1.gif" alt="새창"></a><a
												href="#" onclick="return addCart('${item.item_seq }')"><img
												src="/images/container/md_img_btn2.gif" alt="장바구니"></a>
											<c:choose>
												<c:when test="${item.inquiry_yn eq 'Y' }">
													<a href="javascript:inquery_y();"><img
														src="/images/container/md_img_btn3.gif" alt="바로구매"></a>
												</c:when>
												<c:otherwise>
													<a href="#"
														onclick="return directOrder('${item.item_seq }')"><img
														src="/images/container/md_img_btn3.gif" alt="바로구매"></a>
												</c:otherwise>
											</c:choose>
										</p>
									</div>
									<div class="md_bottom">
										<p class="mb_3">
											<strong>판매가격 : </strong>
											<c:choose>
												<c:when test="${item.inquiry_yn eq 'Y' }">협의</c:when>
												<c:otherwise>
													<c:if test="${item.discount_rate > 0}">
														<span class="c1">${suf:getThousand(item.sale_price) }원</span>
													</c:if>
													<c:if
														test="${item.discount_rate == 0 || empty item.discount_rate}">
														<span class="c1">${suf:getThousand(item.user_price) }원</span>
													</c:if>
												</c:otherwise>
											</c:choose>
										</p>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
				</li>
				<!-- //brand roll_1 -->
			</ul>
		</div>
		<p class="roll_prev">
			<a href="javascript:lastest_part('${prev }', '${param.tab }')"><img src="/images/container/brand_arrow_l.gif" alt="이전"></a>
		</p>
		<p class="roll_next">
			<a href="javascript:lastest_part('${next }', '${param.tab }')"><img src="/images/container/brand_arrow_r.gif" alt="다음"></a>
		</p>
	</div>
</div>
<!-- brand list -->
<script>
	// $("#lastest_cnt").text("${suf:getThousand(data.pagination.totalcount)}");
	$("#lastest_page").html("<b>${param.cpage}</b>/${totalpage}");
	$("#lastest_page").data("totalpage", "${totalpage}");
	function inquery_y() {
		alert("협의가 필요한 물품입니다.\n고객센터로 문의 바랍니다.");
	}
</script>