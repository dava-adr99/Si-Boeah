const express = require("express");
const app = express();
const { pool } = require("./config");
const jwt = require('jsonwebtoken');


app.post("/register", async (req, res) => {
    let { name, email, password, password2 } = req.body;

    console.log({
        name,
        email,
        password,
        password2
    });

    let errors = [];

    if (!name || !email || !password || !password2){
        errors.push({ message: "Please enter all fields" });
    }

    if (password.length < 6) {
        errors.push({ message: "Password should be at least 6 characters" });
    }

    if (password != password2) {
        errors.push({ message: "Password do not match" });
    }

    if(errors.length > 0) {
        res.status(500).send({ errors });
    }else{
        let hashedPassword = await bcrypt.hash(password, 10);
        console.log(hashedPassword);

        await pool.query(
            `SELECT * FROM users 
            WHERE email = $1`, [email], (err, results)=>{
                if (err){
                    throw err;
                }

                console.log(results.rows);
                
                //return user already register
                if(results.rows.length > 0){
                    errors.push({message: "Email already registered "});
                    res.status(500).send({ errors });
                }else{
                    pool.query(
                        `INSERT INTO users (name, email, password)
                        VALUES ($1, $2, $3)
                        RETURNING id, password`, [name, email, hashedPassword], 
                        (err, results)=>{
                            if (err) {
                                throw err;
                            }
                            console.log(results.rows);
                            res.status(201).send({message: "You are now registered. Please login"});
                        }
                    )
                }
            }
        )
    }
});

app.post('/login', async (req, res) => {
    pool.query(
        `SELECT * FROM users WHERE email =$1`, 
        [req.body.email],
        (err, results)=>{
            if (err) {
                throw err;
            }

            console.log(results.rows);

            if (results.rows.length > 0){
                const user = results.rows[0];

                bcrypt.compare(req.body.password, user.password, (err, isMatch)=>{
                    if(err){
                        throw err
                    }

                    if(isMatch){
                        const accessToken = generateAccessToken(user)
                        res.send({token: accessToken})
                    }else{
                        res.send({message: "Password incorrect"})
                    }
                });
            }else{
                res.status(400).send({message: "User not registered"});
            }
        }

    )
})

app.listen(9000, () => {
    console.log(`Server running on port 9000`);
    //console.log(date.toLocaleString('en-GB'));
    //console.log(Date.now())
});