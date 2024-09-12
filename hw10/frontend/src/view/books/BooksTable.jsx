import React from 'react';
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import BasicTable from "../../components/table/TableComponents.jsx";

const BooksTable = () => {
    return (
        <React.Fragment>
            <CssBaseline/>
            <Container maxWidth="lg">
                <Box sx={{bgcolor: 'white', p: 2, borderRadius: 5, mt: 2}}>
                    <BasicTable/>
                </Box>
            </Container>
        </React.Fragment>
    )
}

export default BooksTable;