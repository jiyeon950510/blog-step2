<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <div class="container my-3">
            <form action="/board/${id}/update" method="post" class="mb-1">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Enter title" name="title" id="title"
                        value="${dto.title}">
                </div>

                <div class="form-group">
                    <textarea class="form-control summernote" rows="5" id="content" name="content">
                    ${dto.content}
                </textarea>
                </div>
                <input type="hidden" name="userId" value="${dto.userId}">
            <button type="submit" class="btn btn-primary">글수정완료</button>
            </form>

        </div>
        

        <script>
            $('.summernote').summernote({
                tabsize: 2,
                height: 400
            });

            // function updateById(id){
            //         $.ajax({
            //             type:"updateById",
            //             url:"/board/"+id+"/update"
            //             dataType:"json"
            //         }).done((res)=>{ // 20X 일때
            //             alert(res.msg);
            //             location.href="/";
            //         }).fail((err)=>{ // 40X, 50X 일때
                    
            //             alert(err.responseJSON.msg);
            //         });
            //     }
        </script>

        <%@ include file="../layout/footer.jsp" %>