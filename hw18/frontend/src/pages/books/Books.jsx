import React from 'react';
import BooksTable from "../../view/books/BooksTable.jsx";
import BookTitle from "../../view/books/BookTitle.jsx";

const Books = () => {
    return (
        <React.Fragment>
            <BookTitle/>
            <BooksTable/>
        </React.Fragment>
    )
}

export default Books;