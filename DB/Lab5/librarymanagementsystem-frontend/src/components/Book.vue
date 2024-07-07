<template>
    <el-scrollbar height="100%" style="width: 100%;">

        <!-- 标题和搜索框 -->
        <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold;">
            图书信息查询
        </div>
        <div style="display: flex; align-items: center; justify-content: space-between; width: 90%; margin: 0 auto; padding-top: 2vh;">
            <el-input v-model="this.toQueryCategory" style="flex: 1" size="big" placeholder="输入书本类别"></el-input>
            <el-input v-model="this.toQueryTitle" style="flex: 1; margin-left: 2%" size="big" placeholder="输入书名"></el-input>
            <el-input v-model="this.toQueryPress" style="flex: 1; margin-left: 2%" size="big" placeholder="输入出版社"></el-input>
            <el-input v-model="this.toQueryMinPublishYear" style="flex: 1; margin-left: 2%" size="big" placeholder="输入最小出版年份"></el-input>
            <el-input v-model="this.toQueryMaxPublishYear" style="flex: 1; margin-left: 2%" size="big" placeholder="输入最大出版年份"></el-input>
            <el-input v-model="this.toQueryAuthor" style="flex: 1; margin-left: 2%" size="big" placeholder="输入作者"></el-input>
            <!--<el-upload
                @change="handleFileChange"
                class="upload-demo"
                action="/books"
                :on-success="QueryBooks"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :auto-upload="false"
                style="margin-left: 2%">
                <el-button slot="trigger" type="primary" :icon="Upload"></el-button>
            </el-upload>-->
        </div>
        <div style="display: flex; flex-wrap: wrap; align-items: center; justify-content: space-between; width: 90%; margin: 0 auto; padding-top: 2vh;">
            <el-input v-model="this.toQueryMinPrice" style="flex: 1; min-width: 150px;" size="big" placeholder="输入最小价格"></el-input>
            <el-input v-model="this.toQueryMaxPrice" style="flex: 1; min-width: 150px; margin-left: 2%" size="big" placeholder="输入最大价格"></el-input>
            <el-select v-model="this.toQuerySortBy" style="flex: 1; min-width: 150px; margin-left: 2%" size="big" placeholder="输入排序字段">
                <el-option label="书本 ID" value="bookID"></el-option>
                <el-option label="类别" value="category"></el-option>
                <el-option label="标题" value="title"></el-option>
                <el-option label="出版社" value="press"></el-option>
                <el-option label="出版日期" value="publishYear"></el-option>
                <el-option label="作者" value="author"></el-option>
                <el-option label="价格" value="price"></el-option>
                <el-option label="库存" value="stock"></el-option>
            </el-select>
            <el-select v-model="this.toQuerySortOrder" style="flex: 1; min-width: 150px; margin-left: 2%" size="big" placeholder="输入排序方式">
                <el-option label="升序" value="ASC"></el-option>
                <el-option label="降序" value="DESC"></el-option>
            </el-select>
            <el-button style="flex: 1; min-width: 340px; margin-left: 2%" type="primary" @click="QueryBooks">查询</el-button>
            <el-button type="primary" :icon="Plus" @click="newBookVisible = true" style="flex: none;"></el-button>
        </div>



        <el-dialog v-model="newBookVisible" title="新建书本" width="30%" align-center>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                标题：
                <el-input v-model="newBookInfo.title" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                出版社：
                <el-input v-model="newBookInfo.press" style="width: 12.5vw; margin-left: 24px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                出版年份：
                <el-input v-model="newBookInfo.publishYear" style="width: 12.5vw; margin-left: 9px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                作者：
                <el-input v-model="newBookInfo.author" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                价格：
                <el-input v-model="newBookInfo.price" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                类别：
                <el-input v-model="newBookInfo.category" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                库存：
                <el-input v-model="newBookInfo.stock" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <template #footer>
                <span>
                    <el-button @click="newBookVisible = false">取消</el-button>
                    <el-button type="primary" @click="ConfirmNewBook"
                        :disabled="!isValidNewBookInfo()">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="modifyBookVisible" :title="'修改信息(书本 ID: ' + this.modifyBookInfo.bookID + ')'" width="30%"
            align-center>
            
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                标题：
                <el-input v-model="modifyBookInfo.title" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                出版社：
                <el-input v-model="modifyBookInfo.press" style="width: 12.5vw; margin-left: 24px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                出版年份：
                <el-input v-model="modifyBookInfo.publishYear" style="width: 12.5vw; margin-left: 9px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                作者：
                <el-input v-model="modifyBookInfo.author" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                价格：
                <el-input v-model="modifyBookInfo.price" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                类别：
                <el-input v-model="modifyBookInfo.category" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                库存：
                <el-input v-model="modifyBookInfo.stock" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <template #footer>
                <span>
                    <el-button @click="modifyBookVisible = false">取消</el-button>
                    <el-button type="primary" @click="ConfirmModifyBook"
                        :disabled="!isValidModifyBookInfo()">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="borrowBookVisible" :title="'借阅 ID 为: ' + this.borrowBookInfo.bookID + '的书本'" width="30%"
            align-center>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                借书证ID
                <el-input v-model="borrowBookInfo.cardID" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <div style="margin-left: 2vw; font-weight: bold; font-size: 1rem; margin-top: 20px;">
                借阅时间
                <el-input v-model="borrowBookInfo.borrowTime" style="width: 12.5vw; margin-left: 40px" clearable></el-input>
            </div>
            <template #footer>
                <span>
                    <el-button @click="borrowBookVisible = false">取消</el-button>
                    <el-button type="primary" @click="ConfirmBorrowBook"
                        :disabled="borrowBookInfo.cardID.length === 0 || borrowBookInfo.borrowTime.length === 0">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 结果表格 -->
        <el-table v-if="isShow" :data="tableData" height="600"
            :default-sort="{ prop: 'bookID', order: 'ascending' }" :table-layout="'auto'"
            style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw;">
            <el-table-column prop="bookID" label="书本ID" width="150" sortable></el-table-column>
            <el-table-column prop="category" label="类别" width="150" sortable></el-table-column>
            <el-table-column prop="title" label="书名" width="150" sortable></el-table-column>
            <el-table-column prop="press" label="出版社" width="150" sortable></el-table-column>
            <el-table-column prop="publishYear" label="出版年份" width="150" sortable></el-table-column>
            <el-table-column prop="author" label="作者" width="150" sortable></el-table-column>
            <el-table-column prop="price" label="价格" width="150" sortable></el-table-column>
            <el-table-column prop="stock" label="库存" width="150" sortable></el-table-column>
            <el-table-column label="操作" width="400">
            <template #default="scope">
                <el-button type="primary" :icon="Edit" @click="
                this.modifyBookInfo.bookID=scope.row.bookID;
                this.modifyBookInfo.category=scope.row.category;   
                this.modifyBookInfo.title=scope.row.title;
                this.modifyBookInfo.press=scope.row.press;
                this.modifyBookInfo.publishYear=scope.row.publishYear;
                this.modifyBookInfo.author=scope.row.author;
                this.modifyBookInfo.price=scope.row.price;
                this.modifyBookInfo.stock=scope.row.stock;
                this.modifyBookVisible=true"></el-button>
                <el-button type="primary" :icon="Delete" @click="this.toRemove=scope.row.bookID;this.removeBookVisible=true"></el-button>
                <el-button style="margin-left: 2%;width: 80px " type="primary" @click="
                this.borrowBookInfo.bookID=scope.row.bookID;this.borrowBookVisible=true" v-if="scope.row.stock > 0">借阅</el-button>
            </template>
            </el-table-column>

        </el-table>

        <el-dialog v-model="removeBookVisible" title="删除图书" width="30%">
            <span>确定删除<span style="font-weight: bold;">{{ toRemove }}号书本</span>吗？</span>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="removeBookVisible = false">取消</el-button>
                    <el-button type="danger" @click="ConfirmRemoveBook">
                        删除
                    </el-button>
                </span>
            </template>
        </el-dialog>

    </el-scrollbar>
</template>

<script>
import axios from 'axios';
import { Delete, Edit, Search, Plus, Upload} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
export default {
    data() {
        return {
            isShow: false, 
            tableData: [{ 
                bookID: 1,
                category: "cs",
                title: "db",
                press: "zju",
                publishYear: 2020,
                author: "sjl",
                price: 50.0,
                stock: 100
            }],
            borrowBookVisible: false,
            borrowBookInfo: {
                bookID: 0,
                cardID: 0,
                borrowTime: 20040404
            },
            toQueryCategory: '',
            toQueryTitle: '',
            toQueryPress: '',
            toQueryMinPublishYear: '',
            toQueryMaxPublishYear: '',
            toQueryAuthor: '',
            toQueryMinPrice: '',
            toQueryMaxPrice: '',
            toQuerySortBy: '',
            toQuerySortOrder: '',
            Delete,
            Edit,
            Upload,
            Plus,
            newBookVisible: false,
            removeBookVisible: false, 
            toRemove: 0,
            newBookInfo: {
                category: 'cs',
                title: 'db',
                press: 'zju',
                publishYear: 2004,
                author: 'sjl',
                price: 10,
                stock: 1
            },
            modifyBookVisible: false,
            modifyBookInfo: { 
                bookID: 0,
                category: '',
                title: '',
                press: '',
                publishYear: 0,
                author: '',
                price: 0,
                stock: 1
            },
        }
    },
    methods: {
        async QueryBooks() {
            this.tableData = [] 
            let response = await axios.get('/book', { params: {
                category: this.toQueryCategory,
                title: this.toQueryTitle,
                press: this.toQueryPress,
                minPublishYear: this.toQueryMinPublishYear,
                maxPublishYear: this.toQueryMaxPublishYear,
                author: this.toQueryAuthor,
                minPrice: this.toQueryMinPrice,
                maxPrice: this.toQueryMaxPrice,
                sortBy: this.toQuerySortBy,
                sortOrder: this.toQuerySortOrder
            } })
            let books = response.data 
            console.log(response.data)
            books.forEach(book => { 
                this.tableData.push(book) 
            });
            this.isShow = true 
        },
        ConfirmNewBook() {
            axios.post("/book",
                { 
                    category: this.newBookInfo.category,
                    title: this.newBookInfo.title,
                    press: this.newBookInfo.press,
                    publishYear: this.newBookInfo.publishYear,
                    author: this.newBookInfo.author,
                    price: this.newBookInfo.price,
                    stock: this.newBookInfo.stock
                })
                .then(response => {
                    if (response.data === 1) {
                        ElMessage.success("书本新建成功")
                        this.newBookVisible = false; 
                        this.QueryBooks(); 
                    } else {
                        ElMessage.error("书本新建失败，请检查是否输入了重复或者不合法的字段")
                    }
                    
                })
        },
        ConfirmModifyBook() {
            axios.post("/book", {
                bookID: this.modifyBookInfo.bookID,
                category: this.modifyBookInfo.category,
                title: this.modifyBookInfo.title,
                press: this.modifyBookInfo.press,
                publishYear: this.modifyBookInfo.publishYear,
                author: this.modifyBookInfo.author,
                price: this.modifyBookInfo.price,
                stock: this.modifyBookInfo.stock
            })
            .then(response => {
                if (response.data === 1) {
                    ElMessage.success("书本修改成功");
                    this.modifyBookVisible = false;
                    this.QueryBooks();
                } else {
                    ElMessage.error("书本修改失败，请检查是否输入了重复或者不合法的字段")
                }
            })
        },
        ConfirmRemoveBook() {
            axios.post("/book", {
                bookID : this.toRemove
            })
            .then(response => {
                if (response.data === 1) {
                    ElMessage.success("书本删除成功");
                    this.removeBookVisible = false;
                    this.QueryBooks();
                } else {
                    ElMessage.error("书本删除失败，请检查是否仍有未归还的借书记录")
                }
            })
        },
        ConfirmBorrowBook() {
            axios.post("/borrow", {
                bookID: this.borrowBookInfo.bookID,
                cardId: this.borrowBookInfo.cardID,
                borrowTime: this.borrowBookInfo.borrowTime
            })
            .then(response => {
                if (response.data === 1) {
                    ElMessage.success("书本借阅成功");
                    this.borrowBookVisible = false;
                    this.QueryBooks();
                } else {
                    ElMessage.error("书本借阅失败，请检查借书证是否存在/书本未归还")
                }
            })
        },
        isValidNewBookInfo() {
            return this.newBookInfo.title != "" && 
                    this.newBookInfo.price > 0 && 
                    this.newBookInfo.stock > 0 && 
                    this.newBookInfo.author != "" && 
                    this.newBookInfo.press != "" && 
                    this.newBookInfo.publishYear > 0 ; 
        },
        isValidModifyBookInfo() {
            return this.modifyBookInfo.title != "" && 
                    this.modifyBookInfo.price > 0 && 
                    this.modifyBookInfo.stock >= 0 && 
                    this.modifyBookInfo.author != "" && 
                    this.modifyBookInfo.press != "" && 
                    this.modifyBookInfo.publishYear > 0 ; 
        },
        handleFileChange(file) {
            console.log('File selected:', file);
            this.beforeUpload(file.raw);
        },
        beforeUpload(file) {
            let reader = new FileReader();
            console.log('File selected:', file);
            reader.onload = async (event) => {
                try {
                    let books = JSON.parse(event.target.result); // 确保使用正确的变量名
                    await axios.post('/books', books) // 发送正确的数据变量
                        .then(response => {
                            if (response.data === 1) {
                                ElMessage.success("书本新建成功");
                                this.QueryBooks(); 
                            } else {
                                ElMessage.error("书本新建失败，请检查文件是否符合规范");
                            }
                        });
                } catch (error) {
                    ElMessage.error("处理文件时发生错误：" + error.message);
                }
            };
            reader.readAsText(file);
            return false;
        }
    },
    mounted() { 
        this.QueryBooks()
    }
}
</script>