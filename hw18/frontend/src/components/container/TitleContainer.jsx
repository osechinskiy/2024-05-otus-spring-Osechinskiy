import React from 'react';
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import {Typography} from "@mui/material";

const TitleContainer = (props) => {
    const {text} = props;
    return (
        <React.Fragment>
            <CssBaseline/>
            <Container maxWidth="lg">
                <Box sx={{bgcolor: 'white', p: 2, borderRadius: 5, mt: 2}}>
                    <Typography variant="h4" gutterBottom>
                        {text}
                    </Typography>
                </Box>
            </Container>
        </React.Fragment>
    )
}

export default TitleContainer;