package com.example.finalpro.db;

import com.example.finalpro.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DBManager {
	public static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			String resource = "db/sqlMapConfig.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory =
					new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static List<CustomerVO> findAll() {
		List<CustomerVO> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("customer.findAll");
		session.close();
		return list;
	}

	// ******** admin.ticket ********

	// 메인 페이지에서 카테고리 , 시간 별로 상영작 출력하기
	// time=0은 과거, time=1은 현재, time=2는 미래
	public static List<TicketVO> findAllTicketByCategory(int time, int cateid){
		List<TicketVO> list = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("time", time);
		map.put("cateid", cateid);

		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ticket.findAllTicketByCategory", map);
		session.close();

		return list;
	}
	// admin의 ticketList
	// ticket의 page에 따라 startRecord, endRecord에 해당하는 ticket 목록 출력
	public static List<TicketVO> findTicketPaging(int startRecord, int endRecord){
		List<TicketVO> list = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startRecord", startRecord);
		map.put("endRecord", endRecord);

		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ticket.findTicketPaging", map);
		session.close();

		return list;
	}

	// ticket의 page에 따라 startRecord, endRecord에 해당하는 ticket 목록 출력
	// +search 기능
	public static List<TicketVO> findTicketPagingSearch(int startRecord, int endRecord, String keyword){
		List<TicketVO> list = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startRecord", startRecord);
		map.put("endRecord", endRecord);
		map.put("keyword", keyword);


		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ticket.findTicketPagingSearch", map);
		session.close();

		return list;
	}

	// ticket의 page에 따라 startRecord, endRecord에 해당하는 ticket 목록 출력
	// +search 기능
	// +정렬 기능
	public static List<TicketVO> findTicketPagingSearchOrderBy(int startRecord, int endRecord, String keyword, String order){
		List<TicketVO> list = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startRecord", startRecord);
		map.put("endRecord", endRecord);
		map.put("keyword", keyword);
		map.put("order", order);

		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ticket.findTicketPagingSearchOrderBy", map);
		session.close();

		return list;
	}

	// ticket의 totalRecord를 구하기
	public static int getTotalRecord(String keyword){
		int totalRecord = 0;
		SqlSession session = sqlSessionFactory.openSession();
		totalRecord = session.selectOne("ticket.getTotalRecord", keyword);
		session.close();
		if(totalRecord==0){
			totalRecord = 1;
		}
		return totalRecord;
	}

	public static List<RankingVO> findAllRankingOrderByScore(int cateid){
		List<RankingVO> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ranking.findAllRankingOrderByScore", cateid);

		session.close();

		return list;
	}

	// ticket을 검색
	public static List<TicketVO> findSearchTicket(String keyword){
		List<TicketVO> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("ticket.findSearchTicket", keyword);
		session.close();

		return list;
	}

	// admin에서 ticket을 insert
	public static int insertTicket(TicketVO ticket){
		int re = -1;
		SqlSession session = sqlSessionFactory.openSession();
		re = session.insert("ticket.insertTicket", ticket);
		session.commit();
		session.close();
		return re;
	}
	// admin에서 ticket을 update
	public static int updateTicket(TicketVO ticket){
		int re = -1;
		SqlSession session = sqlSessionFactory.openSession();
		re = session.insert("ticket.updateTicket", ticket);
		session.commit();
		session.close();
		return re;
	}

	// ******** admin.customer ********

	// 고객정보 수정
	public static int updateCustomer(CustomerVO customer){
		int re = -1;
		SqlSession session = sqlSessionFactory.openSession();
		re = session.update("customer.updateCustomer", customer);
		session.commit();
		session.close();
		return re;
	}

	// custid에 따른 qna 작성 리스트
	public static List<QnaVO> listQnaByCustid(String custid){
		List<QnaVO> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("qna.selectQnaByCustid", custid);
		session.close();
		return list;
	}

	public static List<CustomerVO> findCustomerPagingSearch(int startRecord, int endRecord, String keyword){
		List<CustomerVO> list = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startRecord", startRecord);
		map.put("endRecord", endRecord);
		map.put("keyword", keyword);

		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("customer.findCustomerPagingSearch", map);
		session.close();

		return list;
	}

	public static int getTotalCustomerRecord(String keyword){
		int totalRecord = 0;
		SqlSession session = sqlSessionFactory.openSession();
		totalRecord = session.selectOne("customer.getTotalRecord", keyword);
		session.close();
		if(totalRecord==0){
			totalRecord = 1;
		}

		return totalRecord;
	}

//	public static List<CustomerVO> findAll() {
//		List<CustomerVO> list = null;
//		SqlSession session = sqlSessionFactory.openSession();
//		list = session.selectList("customer.findAll");
//		session.close();
//		return list;
//	}

	// ******** Notice ********
	// 공지 등록
	public static int insertNotice(NoticeVO n){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.insert("notice.insert",n);
		session.close();
		System.out.println("DBManager에서 re:"+re);
		return re;
	}

	// 조회수 증가
	public static int updateNoticeHit(int notice_no){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("notice.updateHit",notice_no);
		session.close();
		return re;
	}

	public static int updateNotice(NoticeVO n){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("notice.update",n);
		session.close();
		return re;
	}

	// Notice Paging
	public static int getTotalNoticeRecord(HashMap<String, Object> hashMap){
		int totalRecord=-1;
		SqlSession session=sqlSessionFactory.openSession();
		totalRecord=session.selectOne("notice.getTotalRecord",hashMap);
		session.close();
		return totalRecord;
	}

	public static Object findAllNotice(HashMap<String, Object> hashMap) {
		List<NoticeVO> list=null;
		SqlSession session=sqlSessionFactory.openSession();
		list=session.selectList("notice.findAll", hashMap);
		session.close();
		return list;
	}

	// ******** QNA ********
	// QNA 등록
    public static int insertQna(QnaVO q) {
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.insert("qna.insert",q);
		session.close();
		return re;
    }

	// QNA update
	public static int updateQna(QnaVO qnaVO) {
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("qna.update",qnaVO);
		session.close();
		return re;
	}

	// QNA 답변 작성 및 수정
	public static int updateAnswer(QnaVO q){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("qna.updateAnswer",q);
		session.close();
		return re;
	}

	// QNA 답변 삭제
	public static int deleteAnswer(int qna_no){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("qna.deleteAnswer",qna_no);
		session.close();
		return re;
	}
	// 비공개 QNA일 경우 작성자가 로그인한 사용자와 일치하는지 확인
//	public static int checkWriter(QnaVO q){
//		int re=-1;
//		SqlSession session=sqlSessionFactory.openSession(true);
//		re=session.selectOne("qna.checkWriter",q);
//		session.close();
//		return re;
//	}

	// 해당 사용자가 예매한 티켓 아이디 목록 가져오기 (for QNA)
	public static List<Integer> findTicketidByCustid(String custid){
		List<Integer> list=null;
		SqlSession session=sqlSessionFactory.openSession();
		list=session.selectList("book.findTicketidByCuistid", custid);
		session.close();
		return list;
	}

	// 조회수 증가
	public static int updateQNAHit(int qna_no){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("qna.updateHit",qna_no);
		session.close();
		return re;
	}

	//	페이징 처리를 위한 총 레코드 수 계산
	public static int getTotalQnaRecord(HashMap<String, Object> hashMap){
		int totalRecord=-1;
		SqlSession session=sqlSessionFactory.openSession();
		totalRecord=session.selectOne("qna.getTotalRecord",hashMap);
		session.close();
		return totalRecord;
	}

	public static List<NoticeVO> findAllQna(HashMap<String, Object> hashMap) {
		List<NoticeVO> list=null;
		SqlSession session=sqlSessionFactory.openSession();
		list=session.selectList("qna.findAll", hashMap);
		session.close();
		return list;
	}

	// 알림 생성
	public static int insertNotification(NotificationVO notificationVO){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.insert("notification.insert", notificationVO);
		session.close();
		return re;
	}

	// 알림 불러오기
	public static List<NotificationByCustidVO> findNotificationByCustid(String custid){
		List<NotificationByCustidVO> list=null;
		SqlSession session=sqlSessionFactory.openSession();
		list=session.selectList("notification.findByCustid",custid);
		session.close();
		return list;
	}

	// 체크 안 한 알림 갯수
	public static int countNChecked(){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession();
		re=session.selectOne("notification.countNChecked");
		session.close();
		return re;
	}

	// 알림 checked : n->y\
	public static int updateCheckedToY(){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.update("notification.updateCheckedToY");
		session.close();
		return re;
	}

	// 알림 삭제
	public static int deleteNotification(int notif_no){
		int re=-1;
		SqlSession session=sqlSessionFactory.openSession(true);
		re=session.delete("notification.delete",notif_no);
		session.close();
		return re;
	}
}
