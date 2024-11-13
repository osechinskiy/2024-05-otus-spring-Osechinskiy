import React, {useState} from 'react';
import CommentsByBook from "./CommentsByBook.jsx";
import CommentTitleContainer from "../view/comments/CommentTitleContainer.jsx";

const Comments = () => {
    const [book, setBook] = useState([]);
    return (
        <React.Fragment>
            <CommentTitleContainer text="Комментарии к книге" data={book}/>
            <CommentsByBook action={setBook}/>
        </React.Fragment>
    )
}

export default Comments;