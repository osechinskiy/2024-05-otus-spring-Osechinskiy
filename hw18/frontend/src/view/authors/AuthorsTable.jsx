import React, {useEffect, useState} from 'react';
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import TableContainer from "@mui/material/TableContainer";

const defaultValue = []

const getApiData = async (setData) => {
    const response = await fetch(
        "/api/v1/authors"
    ).then((response) => response.json());
    setData(response);
};

const AuthorsTable = () => {
    const [data, setData] = useState(defaultValue);

    useEffect(() => {
        getApiData(setData);
    }, []);
    return (
        <React.Fragment>
            <CssBaseline/>
            <Container maxWidth="lg">
                <Box sx={{bgcolor: 'white', p: 2, borderRadius: 5, mt: 2}}>
                    <TableContainer component={Paper}>
                        <Table sx={{minWidth: 650}} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="center">ID</TableCell>
                                    <TableCell align="center">Автор</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data && data.map((row) => (
                                    <TableRow key={row.id} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                        <TableCell align="center">{row.id}</TableCell>
                                        <TableCell align="center">{row.fullName}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Container>
        </React.Fragment>
    )
}

export default AuthorsTable;