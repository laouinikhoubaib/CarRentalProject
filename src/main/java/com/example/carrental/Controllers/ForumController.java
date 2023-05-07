package com.example.carrental.Controllers;


import com.example.carrental.Models.*;
import com.example.carrental.Service.ForumService;
import com.example.carrental.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/forum")
public class ForumController {
	
	@Autowired
	ForumService forumService ;
	
	
	@PostMapping("/add-Post/{IdUser}")
	@ResponseBody
	public ResponseEntity<?> addPost_affectedto_User(@RequestBody Post post, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		Long iduser = u.getId();
		post.setCreatedAt(Date.valueOf(LocalDate.now()))	;
		return forumService.addPost(post,iduser);
	}
	@PostMapping("/add-Bad-word")
	@ResponseBody
	public BadWord addPost_affectedto_User(@RequestBody BadWord b) {
	
		return forumService.addBadWord(b);
	}

	
	@PostMapping("/add-Com-to-Com/{IdCom}/{IdUser}")
	@ResponseBody
	public ResponseEntity<?> add_Com_to_Com(@RequestBody PostComment post, @ApiIgnore @AuthenticationPrincipal UserPrincipal u, @PathVariable("IdCom") Long IdCom) {
		return forumService.add_Com_to_Com(post,u.getId(),IdCom);
	}
	
	@PostMapping("/add-Comment/{IdPost}/{IdUser}")
	@ResponseBody
	public ResponseEntity<?> addComment_to_Post(@RequestBody PostComment postComment, @PathVariable("IdPost") Long IdPost, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		postComment.setCommentedAt(Date.valueOf(LocalDate.now()))	;

		return forumService.addComment_to_Post(postComment,IdPost,u.getId());
	}
	
	@PostMapping("/add-Like-post/{IdPost}/{IdUser}")
	@ResponseBody
	public PostLike addLike_to_Post(@RequestBody(required = false) PostLike postLike, @PathVariable("IdPost") Long IdPost, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		PostLike pos1 = new PostLike();
		pos1.setIsLiked(true);
		
		return forumService.addLike_to_Post(pos1,IdPost,u.getId());
	}
	@PostMapping("/add-DisLike-post/{IdPost}/{IdUser}")
	@ResponseBody
	public PostLike addDisLike_to_Post(@RequestBody(required = false) PostLike postLike, @PathVariable("IdPost") Long IdPost, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		PostLike pos1 = new PostLike();
		pos1.setIsLiked(false);
		
		return forumService.addLike_to_Post(pos1,IdPost,u.getId());
	}
	@GetMapping("/get-user-islike-post/{IdPost}")
	@ResponseBody
	public int addDisLike_to_Post( @PathVariable("IdPost") Long IdPost, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		
		return forumService.PostLikeFromUser(IdPost,u.getId());
	}

	@PostMapping("/add-Like-Comment/{IdComment}/{IdUser}")
	@ResponseBody
	public CommentLike addLike_to_Comment(@RequestBody CommentLike commentLike, @PathVariable("IdComment") Long IdComment, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		return forumService.addLike_to_Comment(commentLike,IdComment,u.getId());
	}
	
	
	@PutMapping("/Update-Post/{IdPost}/")
	@ResponseBody
	public ResponseEntity<?> Update_Post(@RequestBody Post post, @PathVariable("IdPost") Long IdPost, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		return forumService.Update_post(post,IdPost,u.getId());
	}
	

	@PutMapping("/Update-Comment/{IdPostCom}/")
	@ResponseBody
	public ResponseEntity<?> Update_Comment(@RequestBody PostComment postComment, @PathVariable("IdPostCom") Long IdPostCom, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		return forumService.Update_Comment(postComment,IdPostCom,u.getId());
	}
	
	@PutMapping("/Update-Likes-Dislikes/{IdLike}")
	@ResponseBody
	public ResponseEntity<?> Swap_like_dislike( @PathVariable("IdLike") Long IdLike) {
		return forumService.Swap_like_dislike(IdLike);
	}

	@GetMapping("/Get-all-Post")
	public List<Post> Get_all_post(){
		return forumService.Get_all_post();
	}

	
	@GetMapping("/Get-Posts-By-user/{IdUser}")
	public Set<Post> Get_post_by_User( @PathVariable("IdUser") Long IdUser){
		return forumService.Get_post_by_User(IdUser);
	}
	
	@DeleteMapping("/Delete-Like/{IdLike}")
	public ResponseEntity<?> Delete_Like( @PathVariable("IdLike") Long IdLike, @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		return forumService.Delete_Like(IdLike,u.getId());
	}
	
	
	@GetMapping("/Get-post-Likes/{IdPost}")
	public Set<PostLike> Get_post_Likes( @PathVariable("IdPost") Long IdPost){
		return forumService.Get_post_Likes(IdPost);
	}
	

	@GetMapping("/Get-post-DisLikes/{IdPost}")
	public Set<PostLike> Get_post_DisLikes( @PathVariable("IdPost") Long IdPost){
		return forumService.Get_post_DisLikes(IdPost);
	}
	
	
	@DeleteMapping("/Delete-Post/{IdPost}")
	public ResponseEntity<?> Delete_Post( @PathVariable("IdPost") Long IdPost/*, @ApiIgnore @AuthenticationPrincipal UserPrincipal u*/) {
		return forumService.Delete_post(IdPost,(long)1/*u.getId()*/);
	}

	@DeleteMapping("/Delete-PostComment/{IdPostCom}")
	public ResponseEntity<?> Delete_PostCom( @PathVariable("IdPostCom") Long IdPostCom,@ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		return forumService.Delete_PostCom(IdPostCom,u.getId());
	}
	//@Scheduled(cron = "*/30 * * * * *")
	@DeleteMapping("/Delete-Post-Redandant")
	public void Delete_post_Redendant( ){
		 forumService.delete_sujet_sans_Int();
	}
	@GetMapping("/Get-best-podt-week")
	public Post Get_best_Post( ) throws MessagingException{
		return forumService.Get_best_Post();
	}
	
	@GetMapping("/Get-Post-Comments/{IdPost}")
	public  Set<PostComment> Get_post_Comm( @PathVariable("IdPost") Long IdPost ){
		return forumService.Get_post_Comm (IdPost);
	}
	
	@GetMapping("/Get-Comment-Comments/{idComment}")
	public  Set<PostComment> Get_comm_Comm( @PathVariable("idComment") Long idComment ){
		return forumService.Get_comm_Comm (idComment);
	}
	
	@PutMapping("/Give-post-etoile/{idPost}/{nb_etoile}")
	public  Post Give_Etoile_Post(@PathVariable("idPost") Long idPost, @PathVariable("nb_etoile") int nb_etoile ){
		return forumService.Give_Etoile_Post (idPost,nb_etoile);
	}
	
	@GetMapping("/Report-Post/{idPost}")
	public  ResponseEntity<?> Report_User(@PathVariable("idPost") Long idPost ,@ApiIgnore @AuthenticationPrincipal UserPrincipal u) throws MessagingException{
		return forumService.Report_User (idPost,u.getId());
	}
	
	@DeleteMapping("/Delete-reported-Post")
	public  void Delete_reported_post() throws MessagingException{
		 forumService.delete_reported_post();
	}
	
	@GetMapping("/Get-More-Likers-User")
	public  Set<Object> Get_more_likers_user(){
		 return forumService.Get_more_likers_user();
	}

	@PutMapping("/Put-test-Data")
	public  void aa(@ApiIgnore @AuthenticationPrincipal UserPrincipal u){
		forumService.DetctaDataLoad("sabri krima",u.getId());
	}

	
	@GetMapping("/Get-Search-post{ch}")
	public  List<Post> adversting_bydata(@PathVariable("ch") String ch,@ApiIgnore @AuthenticationPrincipal UserPrincipal u ){
		return forumService.Searchpost(ch,u.getId());
	}
	
	@GetMapping("/Get-post-report-users/{id}")
	public  Set<User> getre(@PathVariable("id") Long id ){
		return forumService.reportuser(id);
	}

	
	@PostMapping("/api/ocr")
	public String DoOCR(
			@RequestParam("Image") MultipartFile image) throws IOException {
				return forumService.DoOCR(image);

	}	
	
	@PostMapping("/add-Post-image/{idpost}")
	@ResponseBody
	public ResponseEntity<?> addpostimage(@RequestParam MultipartFile image,@PathVariable("idpost") Long idpost) throws IOException {
				return forumService.addimagepost(image,idpost);

	}
	

	@GetMapping("/Get-Post-Details/{idpost}")
	public Post Get_Post_Details(@PathVariable("idpost") Long idpost) {
		return forumService.getPostById(idpost);

}
}

