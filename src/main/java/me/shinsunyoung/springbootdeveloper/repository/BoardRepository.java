package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

//                //findBy(컬럼명)Contiaing 컬럼에 포함된 데이터 검색
//                Page<Board> findByBoardTitleContaining(String searchKeyword, Pageable pageable);


//    제목또는 닉네임으로 문의글 검색
Page<Board> findByBoardTitleContainingOrUser_NicknameContaining(String title, String nickname, Pageable pageable);

    // User 객체를 사용하여 게시물 조회
    List<Board> findByUser(User user);




    //    Optional<List<Board>> findAllByBoardId(Long BoardId);

}
