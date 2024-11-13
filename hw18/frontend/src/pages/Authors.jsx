import React from 'react';
import AuthorsTable from "../view/authors/AuthorsTable.jsx";
import TitleContainer from "../components/container/TitleContainer.jsx";

const Authors = () => {
    return (
        <React.Fragment>
            <TitleContainer text="Информация об авторах"/>
            <AuthorsTable/>
        </React.Fragment>
    )
}

export default Authors;