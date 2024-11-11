import Books from "./pages/books/Books.jsx";
import React from "react";
import Header from "./view/Header.jsx";
import {Routes, Route} from "react-router-dom";
import Authors from "./pages/Authors.jsx";
import Genres from "./pages/Genres.jsx";
import Comments from "./pages/Comments.jsx";
import BookEdit from "./pages/books/BookEdit.jsx";


function App() {
    return (
        <React.Fragment>
            <Header/>
            <Routes>
                <Route path="/" Component={Books}/>
                <Route path="/books" Component={Books}/>
                <Route path="/authors" Component={Authors}/>
                <Route path="/genres" Component={Genres}/>
                <Route path="/comment/:id" Component={Comments}/>
                <Route path="/book/create" Component={BookEdit}/>
                <Route path="/book/:id/edit" Component={BookEdit}/>
            </Routes>
        </React.Fragment>
    );
}

export default App;
